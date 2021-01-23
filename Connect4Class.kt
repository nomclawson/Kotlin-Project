// package com.example.connect_4
import java.lang.IndexOutOfBoundsException
import java.util.*



class Token(val color: String = "red", val name: String = "Player 1", val symbol: String = "0")

class Slot() {
    var index: Int = 0
    var stack = mutableListOf<String>()

    init {
        for (i in 1..6) stack.add(" ")
    }

    fun addToken(token: Token): Boolean {
        return if (index < 6) {
            stack[index] = token.symbol
            index++
            true
        } else {
            println("That slot is full!")
            false
        }
    }
}


class Game() {
    private var board = createBoard()
    private val tokens = mapOf(
        0 to Token("red", "Player 1", "0"),
        1 to Token("black", "Player2", "O")
    )
    private var tokenSelect = 0
        set(value) {
            field = value % 2
        }
    val token: Token?
        get() = tokens[tokenSelect]

    private fun createBoard(): MutableList<Slot> {
        var board = mutableListOf<Slot>()
        for (i in 1..7) {
            var slot = Slot()
            board.add(slot)
        }
        return board
    }

    fun changeTurns() {
        tokenSelect++
    }

    fun addToken(slot: Int): Boolean? {
        // slot should be index (slot number - 1)
        return tokens[tokenSelect]?.let { board[slot].addToken(it) }
    }

    fun displayBoard() {
        println()
        for (i in 5 downTo 0) {
            for (slot in board) {
                print("| ")
                print(slot.stack[i])
                print(" ")
            }
            println("|")
        }
        println("-----------------------------")
        println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |")
        println("=============================")
    }

    fun fourInARow(): Boolean {
        /* Search 2D array left to right, bottom to top
           1) Is any token in slot
           2) Check 8 directions
           3) Pursue valid directions
        */

        // Loop through slots
        for (i in 0 until board.size) {

            for (j in 0 until board[i].stack.size) {
                // Check if not empty
                if (getValue(i,j) == " ") break
                if (checkAllAdjacent(i,j)) return true
            }
        }
        return false
    }

    private fun getValue(x: Int, y: Int): String {
        return try{
            board[x].stack[y]
        }
        catch (e: IndexOutOfBoundsException){
            " "
        }
    }

    private fun checkAdjacent(x: Int,y: Int, dx: Int, dy: Int): Boolean {
        return (getValue(x,y) == getValue(dx,dy))
    }

    private fun checkAllAdjacent(x: Int, y: Int): Boolean {
        // Check up
        for (i in 1..4){

            if (!checkAdjacent(x,y,x,y+i)) {
                break
            }
            else if (i == 3) {
                return true
            }
        }
        // Check up right
        for (i in 1..4){
            if (!checkAdjacent(x,y,x+i,y+i)) {
                break
            }
            else if (i == 3) {
                return true
            }
        }
        // Check right
        for (i in 1..4){
            if (!checkAdjacent(x,y,x+i,y)) {
                break
            }
            else if (i == 3) {
                return true
            }
        }
        // Check down right
        for (i in 1..4){
            if (!checkAdjacent(x,y,x+i,y-i)) {
                break
            }
            else if (i == 3) {
                return true
            }
        }
        // Check down
        for (i in 1..4){
            if (!checkAdjacent(x,y,x,y-i)) {
                break
            }
            else if (i == 3) {
                return true
            }
        }
        // Check down left
        for (i in 1..4){
            if (!checkAdjacent(x,y,x-i,y-i)) {
                break
            }
            else if (i == 3) {
                return true
            }
        }
        // Check left
        for (i in 1..4){
            if (!checkAdjacent(x,y,x-i,y)) {
                break
            }
            else if (i == 3) {
                return true
            }
        }
        // Check up left
        for (i in 1..4){
            if (!checkAdjacent(x,y,x-i,y+i)) {
                break
            }
            else if (i == 3) {
                return true
            }
        }
        return false
    }

}


fun main(args: Array<String>) {
    var gameBoard = Game()

    displayWelcome()

    gameBoard.displayBoard()

    var first = true
    var slot = 0
    do {
        if (!first) gameBoard.changeTurns()
        do {
            slot = promptUser(gameBoard.token)
            var valid = gameBoard.addToken(slot)
        } while (!valid!!)
        gameBoard.displayBoard()
        first = false
    } while (!gameBoard.fourInARow())
    displayWinner(gameBoard)

}

fun displayWinner(gameBoard: Game) {
    println("\n")
    println("****************************")
    println("      ${gameBoard.token?.name} won!!!")
    println("****************************")
    println()
    readLine()
}

fun displayWelcome(){
    println()
    println("===========================")
    println("     --- CONNECT 4 ---     ")
    println("===========================")
    println("  Get 4 in a row to win!")
    println()
    println("(Press enter to continue.)")
    print(">>>")
    readLine()
    println()
}

fun promptUser(token: Token?): Int {

    print("${token!!.name} ( ${token.symbol} ) ")

    var slotSelect = 0
    var first = true
    var err = false
    val errMsg = "Please enter positive int between 1-7:"

    do {
        if (!first or err) println(errMsg)
        print(">>> ")
        try {
            slotSelect = readLine()!!.toInt()
        } catch (e: NumberFormatException) {
            // println(errMsg)
            err = true
            slotSelect = 0
        }
        first = false
    } while ((slotSelect < 1) or (slotSelect > 7))
    return --slotSelect
}