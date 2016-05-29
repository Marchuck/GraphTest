package topics.som;

/**
 * @author Lukasz
 * @since 29.05.2016.
 */
public class Constants {

    public final static int constWindowWidth = 400;
    public final static int constWindowHeight = 400;

    public final static int constNumCellsAcross = 40;
    public final static int constNumCellsDown = 40;


    //number of weights each node must contain. One for each element of
//the input vector. In this example it is 3 because a color is
//represented by its red, green and blue components. (RGB)
    public final static
    int constSizeOfInputVector = 4;

    //the number of epochs desired for the training
    public final static
    int constNumIterations = 1000;

    //the value of the learning rate at the start of training
    public final static
    double constStartLearningRate = 0.1;

/*   uncomment the following if you'd like the SOM to classify randomly created training sets  */

//#define RANDOM_TRAINING_SETS

    public final static
    int constMaxNumTrainingSets = 20;
    public final static
    int constMinNumTrainingSets = 5;

    public static final int IDI_ICON1 = 101;
    public static final int _APS_NEXT_RESOURCE_VALUE = 102;
    public static final int _APS_NEXT_COMMAND_VALUE = 40001;
    public static final int APS_NEXT_CONTROL_VALUE = 1000;
    public static final int _APS_NEXT_SYMED_VALUE = 101;

}
