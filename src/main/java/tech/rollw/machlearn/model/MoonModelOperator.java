/*
 * Copyright (C) 2022 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.rollw.machlearn.model;

import tech.rollw.machlearn.data.dto.ModelEvaluationResult;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * 月球形状分类模型
 *
 * @author RollW
 */
public class MoonModelOperator extends BaseModelEvaluator
        implements ModelEvaluator, ModelPredictor {
    private MultiLayerConfiguration conf;
    private MultiLayerNetwork model;

    public MoonModelOperator() {
        initModelConf();
    }

    @Override
    public Model getModel() {
        return BuiltinModel.MOON;
    }

    @Override
    public ModelData predict(ModelData input) {
        if (model == null) {
            evalModel(100);
        }
        INDArray array = model.output(input.toINDArray());
        return new D4jModelData(array);
    }

    private void initModelConf() {
        conf = new NeuralNetConfiguration.Builder()
                .seed(50505)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(0.005, 0.9))
                .list()
                .layer(new DenseLayer.Builder().nIn(2).nOut(50)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX)
                        .nIn(50).nOut(2).build())
                .build();
    }

    @Override
    public ModelEvaluationResult evalModel(int trainMultiple) {
        if (testIter == null || trainIter == null) {
            try {
                loadLocalData();
            } catch (IOException | InterruptedException e) {
                throw new ModelException(e);
            }
        }
        return evalModel(trainMultiple, trainIter, testIter);
    }

    public ModelEvaluationResult evalModel(int trainMultiple,
                                           DataSetIterator trainSet,
                                           DataSetIterator testSet) {
        model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(100));
        model.fit(trainSet, trainMultiple);
        Evaluation eval = model.evaluate(testSet);
        return new ModelEvaluationResult(
                eval.accuracy(), eval.topNAccuracy(),
                eval.precision(), eval.recall(),
                eval.f1(),
                ModelHelper.convertToMatrixInt(eval.getConfusionMatrix()));
    }

    private void loadLocalData() throws IOException, InterruptedException {
        ClassPathResource classPathResource = new ClassPathResource("/moon_data_train.csv");
        ClassPathResource evalResource = new ClassPathResource("/moon_data_eval.csv");
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(classPathResource.getFile()));
        trainIter = new RecordReaderDataSetIterator(
                rr, 50, 0, 2);

        //Load the test/evaluation data:
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(evalResource.getFile()));
        testIter = new RecordReaderDataSetIterator(
                rrTest, 50, 0, 2);
    }

}
