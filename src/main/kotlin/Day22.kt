import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day22()

@ExperimentalTime
object Day22 : Day(22, testData = false, "") {
    override fun invoke() {
        val input = getInputData { it }

        part1(input) { data ->

            var player = ""
            val deal = mutableMapOf<String, MutableList<Int>>()
            data.forEach { line ->
                when {
                    line.startsWith("Player") -> {
                        player = line.dropLast(1)
                        deal[player] = mutableListOf()
                    }
                    line.toIntOrNull() != null && player.isNotBlank() ->
                        deal[player]!!.add(line.toInt())
                }
            }
            val player1 = mutableListOf<Int> ()// deal["Player 1"]!!
            val player2 = deal["Player 2"]!!

            while (player1.isNotEmpty() && player2.isNotEmpty()) {
                val cardP1 = player1.removeFirst()
                val cardP2 = player2.removeFirst()
                if (cardP1 > cardP2) {
                    player1.add(cardP1)
                    player1.add((cardP2))
                } else {
                    player2.add((cardP2))
                    player2.add((cardP1))
                }
            }

            println("P1 $player1")
            println("P2 $player2")

            val player1Result = player1.mapIndexed { idx, card ->
                card * (player1.size - idx)
            }.sum()

            val player2Result = player2.mapIndexed { idx, card ->
                card * (player2.size - idx)
            }.sum()

            "Result: Player 1: $player1Result | Player 2: $player2Result"
        }

        part2(input) { data ->
            var player = ""
            val deal = mutableMapOf<String, MutableList<Int>>()
            data.forEach { line ->
                when {
                    line.startsWith("Player") -> {
                        player = line.dropLast(1)
                        deal[player] = mutableListOf()
                    }
                    line.toIntOrNull() != null && player.isNotBlank() ->
                        deal[player]!!.add(line.toInt())
                }
            }
            val player1 = deal["Player 1"]!!
            val player2 = deal["Player 2"]!!

            println("P1: $player1 - ${player1.size}")
            println("P2: $player2 - ${player2.size}")

            val winner = playGame(player1.toList(), player2.toList())
            println("Winner: $winner - count: ${winner.second.size}")

            val points = winner.second.mapIndexed { idx, card ->
                card.toLong() * (winner.second.size - idx)
            }
            println("Points: $points")

            "Points: ${points.sum()}"
        }
    }

    fun playGame(p1: List<Int>, p2: List<Int>): Pair<Int, List<Int>> {
        var gameOver = false
        val gameHistory = mutableListOf<Pair<List<Int>,List<Int>>>()
        var winner = 0
        val deckPlayer1 = p1.toMutableList()
        val deckPlayer2 = p2.toMutableList()

        // println("Game: Start")
        while (!gameOver) {
            // println("P1: $deckPlayer1")
            // println("P2: $deckPlayer2")

            if (gameHistory.any { it.first == deckPlayer1 && it.second == deckPlayer2 }) {
                winner = 1
                gameOver = true
                // println("Game already in history: $gameHistory")
            } else {
                when  {
                    deckPlayer1.isEmpty() -> {
                        gameOver = true
                        winner = 2
                    }
                    deckPlayer2.isEmpty() -> {
                        gameOver = true
                        winner = 1
                    }
                    else -> {
                        gameHistory.add(deckPlayer1.toList() to deckPlayer2.toList())
                        val dealP1 = deckPlayer1.removeFirst()
                        val dealP2 = deckPlayer2.removeFirst()

                        //subgame ?
                        val dealWinner = if (dealP1 <= deckPlayer1.size && dealP2 <= deckPlayer2.size) {
                            playGame(deckPlayer1.toList(), deckPlayer2.toList()).first
                        } else {
                            if (dealP1 > dealP2) 1 else 2
                        }

                        //println("Deal: $dealP1 vs $dealP2 winner: $dealWinner")

                        if (dealWinner == 1) {
                            deckPlayer1.add(dealP1)
                            deckPlayer1.add(dealP2)
                        } else {
                            deckPlayer2.add(dealP2)
                            deckPlayer2.add(dealP1)
                        }
                    }
                }
            }
        }

        return winner to if (winner == 1) deckPlayer1.toList() else deckPlayer2.toList()
    }
}