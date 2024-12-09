package com.example.simonsays

import android.os.Parcel
import android.os.Parcelable
import android.widget.Button

// The length of time the each button in a sequence is shown
/** A difficulty of 1.00 */
const val EASY = 1.00
/** A difficulty of 1.50 */
const val MED = 1.50
/** A difficulty of 2.00 */
const val HARD = 2.00

/**
 * Represents a single *Game* object.
 *
 * @param difficulty Determines the timer difficulty for the game.
 * @property sequence A list of Buttons to keep track of the button order. The String stored is the button name.
 * @property turn The current turn.
 * @property timer The amount of time the player has to make a play.
 * @constructor Creates a [Game] with [difficulty] set to [EASY] if otherwise not provided.
 * */
class Game(var difficulty: Double) : Parcelable {
    var sequence: MutableList<Button> = mutableListOf()
    private var turn = 0
    var currentIndex = 0
    private val timer= 5.00 /  difficulty
    var gameOver = false

    // I found some good information about this here https://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-parcelable

    /**
     * Constructor for creating a Game object from a Parcel.
     *
     * @param parcel The Parcel to read the object's data from.
     */
    constructor(parcel: Parcel) : this(
        parcel.readDouble()
    ) {
        turn = parcel.readInt()
    }

    /**
     * Write the Game object to a Parcel.
     *
     * @param parcel The Parcel to write the object's data into.
     * @param flags Additional flags about how the object should be written.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(difficulty)
        parcel.writeInt(turn)
    }

    /**
     * Describe the contents of the Parcel.
     *
     * @return An integer bitmask indicating the set of special object types marshalled by the Parcel.
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Companion object to generate instances of the Parcelable class from a Parcel.
     */
    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }
}
