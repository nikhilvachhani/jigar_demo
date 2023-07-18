package com.frami.utils.widget.ratio

enum class FixedAttribute
/**
 * Default constructor for FixedAttribute
 * @param id Integer with the enum value
 */(
    /**
     * Represents the enum type
     */
    val id: Int
) {
    /**
     * Has two integer enums
     * <br></br>One for the ratio height
     * <br></br>One for the ratio width
     */
    HEIGHT(0),
    WIDTH(1);

    companion object {
        /**
         * Returns a FixedAttribute that corresponds to a certain enum value
         * <br></br>Throws *IllegalArgumentException* if Integer is higher than the current enum values
         * (In this case Integer must be lower than 2)
         * @param id Integer with the enum value
         * @return FixedAttribute that corresponds to a following enum value
         */
        fun fromId(id: Int): FixedAttribute {
            for (f in FixedAttribute.values()) {
                if (f.id == id) return f
            }
            throw IllegalArgumentException()
        }
    }

}