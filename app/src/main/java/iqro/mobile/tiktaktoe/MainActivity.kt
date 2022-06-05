package iqro.mobile.tiktaktoe

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import iqro.mobile.tiktaktoe.databinding.ActivityMainBinding
import iqro.mobile.tiktaktoe.databinding.WinDialogLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isXTerm = true
    private var xWinCount = 0
    private var oWinCount = 0
    private var drawCount = 0
    private val tableChar = Array(3) { CharArray(3) { '*' } }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onClickBtn(view: View) {
        view as Button
        if (view.text.isNotEmpty()) return
        val char = if (isXTerm) {
            view.setTextColor(Color.RED)
            'X'
        } else {
            view.setTextColor(Color.GREEN)
            'O'
        }
        val pair = getPositionNumber(view)
        tableChar[pair.first][pair.second] = char
        view.text = char.toString()
        checkWin()
        isXTerm = !isXTerm
        updateTurn()

    }

    private fun checkWin() {
        var isWin = false
        var tapped = 0
        var winChar = 'X'
        for (i in 0 until 3) {
            if (tableChar[i][0] == tableChar[i][1] && tableChar[i][1] == tableChar[i][2] && tableChar[i][0] != '*') {
                winChar = tableChar[i][0]
                isWin = true
                break
            }
        }
        for (j in 0 until 3) {
            if (tableChar[0][j] == tableChar[1][j] && tableChar[1][j] == tableChar[2][j] && tableChar[0][j] != '*') {
                winChar = tableChar[0][j]
                isWin = true
                break
            }
        }
        if (tableChar[0][0] == tableChar[1][1] && tableChar[1][1] == tableChar[2][2] && tableChar[0][0] != '*') {
            winChar = tableChar[0][0]

            isWin = true
        }
        if (tableChar[2][0] == tableChar[1][1] && tableChar[1][1] == tableChar[0][2] && tableChar[2][0] != '*') {
            winChar = tableChar[2][0]
            isWin = true
        }
        if (isWin) {
            if (winChar == 'X') xWinCount++
            else oWinCount++
            updateCountTitles()
            showDialog(winChar)
        } else {
            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    if (tableChar[i][j] != '*') tapped++
                }
            }
            if (tapped == 9) {
                drawCount++
                updateCountTitles()
                showDialog('*')
            }
        }
    }

    private fun updateTurn() {
        binding.turnTv.text =
            if (isXTerm) getString(R.string.xTurnTitle) else getString(R.string.oTurnTitle)
    }

    private fun updateCountTitles() {
        binding.xWinsCountTv.text = "$xWinCount"
        binding.oWinsCountTv.text = "$oWinCount"
        binding.drawCountTv.text = "$drawCount"
    }

    private fun showDialog(winChar: Char) {
        val dialogLayoutBinding = WinDialogLayoutBinding.inflate(LayoutInflater.from(this))
        val title = when (winChar) {
            'X' -> {
                getString(R.string.xWon)
            }
            'O' -> {
                getString(R.string.oWon)
            }
            else -> {
                getString(R.string.draw)
            }
        }
        dialogLayoutBinding.titleTv.text = title
        val dialog = AlertDialog.Builder(this, R.style.RoundedDialogStyle)
            .setView(dialogLayoutBinding.root)
            .setCancelable(false)
            .create()
        dialog.show()
        dialogLayoutBinding.playBtn.setOnClickListener {
            resetGame()
            dialog.dismiss()
        }
    }

    private fun resetGame() {
        for (i in tableChar.indices) {
            for (j in tableChar[0].indices) {
                tableChar[i][j] = '*'
            }
        }
        binding.tableView.children.forEach { row ->
            (row as TableRow).children.forEach {
                val button = it as Button
                button.text = ""
            }

        }
    }

    private fun getPositionNumber(view: View): Pair<Int, Int> {
        return when (view.id) {
            R.id.firstBtn -> Pair(0, 0)
            R.id.secondBtn -> Pair(0, 1)
            R.id.thirdBtn -> Pair(0, 2)
            R.id.forthBtn -> Pair(1, 0)
            R.id.fifthBtn -> Pair(1, 1)
            R.id.sixBtn -> Pair(1, 2)
            R.id.sevenBtn -> Pair(2, 0)
            R.id.eightBtn -> Pair(2, 1)
            else -> Pair(2, 2)
        }
    }
}