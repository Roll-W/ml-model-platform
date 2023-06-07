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

import tech.rollw.machlearn.MachineLearningApplication;
import tech.rollw.machlearn.data.dto.ModelEvaluationResult;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.InputStreamInputSplit;
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
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.TestDataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author RollW
 */
public class IrisModelOperator extends BaseModelEvaluator implements
        SingleDataSetModelEvaluator, ModelPredictor {
    private static final Logger logger = LoggerFactory.getLogger(IrisModelOperator.class);

    private MultiLayerNetwork model;
    private MultiLayerConfiguration conf;

    public IrisModelOperator() {
        initModel();
    }

    private void initModel() {
        logger.info("Initialize iris model...");
        // TODO: set by user
        conf = new NeuralNetConfiguration.Builder()
                .seed(50202)
                .activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(0.1))
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder().nIn(4).nOut(3)
                        .build())
                .layer(new DenseLayer.Builder().nIn(3).nOut(3)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        // Override the global TANH activation with softmax for this layer
                        .nIn(3).nOut(3).build())
                .build();
    }

    @Override
    public Model getModel() {
        return BuiltinModel.IRIS;
    }

    @Override
    public ModelData predict(ModelData input) {
        if (model == null) {
            evalModel(100);
        }
        INDArray array = model.output(input.toINDArray(), false);
        return new D4jModelData(array);
    }

    private ModelEvaluationResult evalModel(int trainMultiple,
                                            DataSetIterator trainSet,
                                            DataSetIterator testSet) {
        DataNormalization normalizer = new NormalizerStandardize();
        normalizer.fit(trainSet);
        trainSet.setPreProcessor(normalizer);
        testSet.setPreProcessor(normalizer);
        model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(100));

        logger.info("Now evaluate the iris model.");
        model.fit(trainSet, trainMultiple);
        Evaluation eval = model.evaluate(testSet);

        return new ModelEvaluationResult(
                eval.accuracy(),
                eval.topNAccuracy(),
                eval.precision(),
                eval.recall(),
                eval.f1(),
                ModelHelper.convertToMatrixInt(eval.getConfusionMatrix()));
    }

    @Override
    public ModelEvaluationResult evalModel(int trainMultiple) {
        if (trainIter == null || testIter == null) {
            try {
                loadLocalDataSet(-1);
            } catch (IOException | InterruptedException e) {
                throw new ModelException(e);
            }
        }
        return evalModel(trainMultiple, trainIter, testIter);
    }

    @Override
    public ModelEvaluationResult evalModel(int trainMultiple, double fractionTrain) {
        if (trainIter == null || testIter == null) {
            try {
                loadLocalDataSet(fractionTrain);
            } catch (IOException | InterruptedException e) {
                throw new ModelException(e);
            }
        }
        return evalModel(trainMultiple, trainIter, testIter);
    }

    private void loadLocalDataSet(double factor) throws IOException, InterruptedException {
        RecordReader recordReader = new CSVRecordReader(0, ',');
        recordReader.initialize(new InputStreamInputSplit(
                MachineLearningApplication.loadResource("/iris.txt")));

        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,
                150, 4, 3);
        DataSet allData = iterator.next();
        allData.shuffle();
        if (factor <= 0 || factor >= 1) {
            factor = 0.65;
        }
        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(factor);
        org.nd4j.linalg.dataset.DataSet trainingData = testAndTrain.getTrain();
        org.nd4j.linalg.dataset.DataSet testData = testAndTrain.getTest();
        trainIter = new TestDataSetIterator(trainingData);
        testIter = new TestDataSetIterator(testData);
    }
}
