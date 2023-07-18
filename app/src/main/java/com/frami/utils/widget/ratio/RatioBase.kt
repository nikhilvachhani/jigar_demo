package com.frami.utils.widget.ratio


/**
 * Interface for Ratio bases
 * <br></br>Currently not being used
 * @author Ritesh Shakya
 */
interface RatioBase {
    /**
     * Sets a new Ratio with certain horizontal and vertical ratio values
     * @param horizontalRatio float with horizontal ratio
     * @param verticalRatio float with vertical ratio
     */
    fun setRatio(horizontalRatio: Float, verticalRatio: Float)

    /**
     * Sets a new ratio with a certain FixedAttribute and certain horizontal and vertical ratio
     * values
     * @param fixedAttribute FixedAttribute with a certain fixed attribute
     * @param horizontalRatio float with horizontal ratio
     * @param verticalRatio float with vertical ratio
     */
    fun setRatio(
        fixedAttribute: FixedAttribute,
        horizontalRatio: Float,
        verticalRatio: Float
    )

    /**
     * Returns current horizontal ratio
     * @return float with the current horizontal ratio
     */
    val horizontalRatio: Float

    /**
     * Returns current vertical ratio
     * @return float with the current vertical ratio
     */
    val verticalRatio: Float

    /**
     * Returns current fixed attribute
     * @return FixedAttribute with the current fixed attribute
     */
    val fixedAttribute: FixedAttribute?
}