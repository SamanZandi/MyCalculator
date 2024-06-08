package com.zandroid.mycalculator.ui

import com.zandroid.mycalculator.R
import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.zandroid.mycalculator.databinding.ActivityCalculatorBinding
import com.zandroid.mycalculator.room.CalcEntity
import com.zandroid.mycalculator.viewModel.HistoryViewModel
import com.zandroid.mycalculator.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function
import net.objecthunter.exp4j.operator.Operator

import javax.inject.Inject
import kotlin.math.acos
import kotlin.math.acosh
import kotlin.math.asin
import kotlin.math.asinh
import kotlin.math.atan
import kotlin.math.atanh
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.sin
import kotlin.math.tan

@AndroidEntryPoint
class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding

    private val mainViewModel: MainViewModel by viewModels()

    private val historyViewModel:HistoryViewModel by viewModels()

    @Inject
    lateinit var entity: CalcEntity

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnAc.setOnClickListener { clearAll() }
            btnBackSpace.setOnClickListener { backspace() }
            defineOperators()
            digitInit()
            txtCalculation.movementMethod = ScrollingMovementMethod()
            updateTextview()
        }
    }

    ///Remove Zeros
    @RequiresApi(Build.VERSION_CODES.N)
    fun removeTrailingZeros(number: Double): String {
        val decimalFormat = DecimalFormat("#.##########")
        return decimalFormat.format(number)
    }
    /// Calculate
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    fun calculateResult() {
        try {
            var txtExpression = binding.txtCalculation.text.toString()
            if (txtExpression.contains('÷')) {
                txtExpression = txtExpression.replace('÷', '/')
            }
            if (txtExpression.contains('×')) {
                txtExpression = txtExpression.replace('×', '*')
            }

            val expressionBuilder = ExpressionBuilder(txtExpression)

            val cosFunction = object : Function("cos", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "cosine function requires one argument" }
                    if (binding.btnRad?.text == "Rad") {
                        return cos((args[0]))
                    } else {
                        return cos(Math.toRadians(args[0]))
                    }
                }
            }

            val acosFunction = object : Function("acos", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "acos function requires one argument" }
                    if (binding.btnRad?.text == "Rad") {
                        return acos((args[0]))
                    } else {
                        return Math.toDegrees(acos(args[0]))
                    }
                }
            }


            val acoshFunction = object : Function("acosh", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "acosh function requires one argument" }
                    return acosh((args[0]))
                }
            }
            val sinFunction = object : Function("sin", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "sin function requires one argument" }
                    if (binding.btnRad?.text == "Rad") {
                        return sin((args[0]))
                    } else {
                        return sin(Math.toRadians(args[0]))
                    }
                }
            }

            val asinFunction = object : Function("asin", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "asin function requires one argument" }
                    if (binding.btnRad?.text == "Rad") {
                        return asin((args[0]))
                    } else {
                        return Math.toDegrees(asin(args[0]))
                    }
                }
            }

            val asinhFunction = object : Function("asinh", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "asinh function requires one argument" }
                    return asinh((args[0]))
                }
            }

            val tanFunction = object : Function("tan", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "tan function requires one argument" }
                    if (binding.btnRad?.text == "Rad") {
                        return tan((args[0]))
                    } else {
                        return tan(Math.toRadians(args[0]))
                    }
                }
            }
            val atanFunction = object : Function("atan", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "atan function requires one argument" }
                    if (binding.btnRad?.text == "Rad") {
                        return atan((args[0]))
                    } else {
                        return Math.toDegrees(atan(args[0]))
                    }
                }
            }

            val atanhFunction = object : Function("atanh", 1) {
                override fun apply(vararg args: Double): Double {
                    require(args.size == 1) { "atanh function requires one argument" }
                    return atanh((args[0]))
                }
            }

            val log = object : Function("lg", 1) {
                override fun apply(vararg args: Double): Double {
                    return log10(args[0])
                }
            }


            val ln = object : Function("ln", 1) {
                override fun apply(vararg args: Double): Double {
                    return ln(args[0])
                }
            }

            val factorial: Operator = object : Operator("!", 1, true, PRECEDENCE_POWER + 1) {
                override fun apply(vararg args: Double): Double {
                    val arg = args[0].toInt()
                    require(arg.toDouble() == args[0]) { "Operand for factorial has to be an integer" }
                    require(arg >= 0) { "The operand of the factorial can not be less than zero" }
                    var result = 1.0
                    for (i in 1..arg) {
                        result *= i.toDouble()
                    }
                    return result
                }
            }

    /*          val percentage: Operator = object : Operator("%", 2, true, PRECEDENCE_POWER + 1) {
                    override fun apply(vararg args: Double): Double {

                        if (txtExpression.last() == '%') {
                            val result = args[0] + ((args[0] * args[1]) / 100)
                            Log.e("apply: ", "=$result")
                        //    binding.txtResult.text = "=$result"
                         //   mainViewModel.resultText = binding.txtResult.text.toString()
                            return result
                        } else {
                            val result = (args[0] / 100) + args[1]
                            Log.e("apply: ", "=$result")
                          //  binding.txtResult.text = "=$result"
                         //   mainViewModel.resultText = binding.txtResult.text.toString()
                            return result
                        }
                    }
                }
*/

            val percentageOne: Operator = object : Operator("%", 1, true, PRECEDENCE_POWER+1 ) {
                override fun apply(vararg args: Double): Double {
                    return args[0]/100
                }
    }


                val result = expressionBuilder.function(tanFunction).function(atanFunction)
                    .function(atanhFunction).function(acoshFunction)
                    .function(log).function(ln).function(cosFunction)
                    .function(acosFunction).function(asinhFunction)
                    .function(asinFunction).function(sinFunction).operator(factorial)
                    .operator( percentageOne)
                    .build().evaluate()


                val resultInt = result.toInt()
                if (result == resultInt.toDouble()) {
                    mainViewModel.resultText = "=${removeTrailingZeros(resultInt.toDouble())}"
                    binding.txtResult.text = mainViewModel.resultText

                } else {
                    mainViewModel.resultText = "=${removeTrailingZeros(result)}"
                    binding.txtResult.text = mainViewModel.resultText
                }

            } catch (ext: ArithmeticException) {
                Toast.makeText(this@CalculatorActivity, "${ext.message}", Toast.LENGTH_SHORT).show()
            } catch (exception: Exception) {
         /*       Toast.makeText(this@CalculatorActivity, "Invalid format", Toast.LENGTH_SHORT)
            .show()*/
               binding.txtResult.text="=${exception.message}"

            }catch (exception: NumberFormatException) {
            /*       Toast.makeText(this@CalculatorActivity, "Invalid format", Toast.LENGTH_SHORT)
               .show()*/
            binding.txtResult.text="=${exception.message}"
        }
    }


    @SuppressLint("SetTextI18n")
    private fun digitInit() {
        binding.apply {
            // digit click
            btn0.setOnClickListener { appendText("0")}
            btn1.setOnClickListener { appendText("1")}
            btn2.setOnClickListener { appendText("2") }
            btn3.setOnClickListener { appendText("3") }
            btn4.setOnClickListener { appendText("4") }
            btn5.setOnClickListener { appendText("5") }
            btn6.setOnClickListener { appendText("6") }
            btn7.setOnClickListener { appendText("7")}
            btn8.setOnClickListener { appendText("8") }
            btn9.setOnClickListener { appendText("9") }
            btnOpenBracket.setOnClickListener { appendText("(") }
            btnCloseBracket.setOnClickListener { appendText(")") }

            binding.btnPNSign.setOnClickListener {
                handlePlusMinusButtonClick()
                //appendText("(-")
            }
            btnSqrt?.setOnClickListener { appendText("sqrt(") }
            btnSin?.setOnClickListener { appendText("sin(") }
            btnCos?.setOnClickListener { appendText("cos(") }
            btnTan?.setOnClickListener { appendText("tan(") }
            btnLn?.setOnClickListener { appendText("ln(") }
            btnLog?.setOnClickListener { appendText("log10(") }
            btnDivideByOne?.setOnClickListener { appendText("1/") }
            btnPow2?.setOnClickListener { appendText("^2") }
            btnPow?.setOnClickListener { appendText("^") }
            btnAbs?.setOnClickListener { appendText("abs(") }
            btnPi?.setOnClickListener { appendText("π") }
            btnE?.setOnClickListener { appendText("e") }
        }
    }


    //Clear All
    private fun clearAll() {
        mainViewModel.calculationText="0"
        mainViewModel.resultText="0"
        updateTextview()
    }

    //Back Space Button
    @RequiresApi(Build.VERSION_CODES.N)
    private fun backspace() {
        //back space
        binding.apply {
            val expressionCalculation =mainViewModel.calculationText
            txtCalculation.text=expressionCalculation
            val expressionResult =  mainViewModel.resultText
            txtResult.text=expressionResult
            if (expressionCalculation.isNotEmpty() ){

                if (expressionCalculation== expressionResult) {

                    val deletedCharacter = expressionCalculation.substring(0, expressionCalculation.length - 1)
                    mainViewModel.calculationText=deletedCharacter

                    val deletedCharacterResult = expressionResult.substring(0, expressionResult.length - 1)
                    mainViewModel.resultText=deletedCharacterResult
                    updateTextview()
                }else {
                        val deletedCharacter = expressionCalculation.substring(0, expressionCalculation.length - 1)
                        mainViewModel.calculationText=deletedCharacter
                        txtCalculation.text = mainViewModel.calculationText
                        calculateResult()
              }

            }else {
                clearAll()
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun handlePlusMinusButtonClick() {
        val expression = binding.txtCalculation.text.toString()
        appendText("(-")
        if (expression.contains("(-")) {
            // If the expression starts with '-', remove it
            binding.txtCalculation.text = expression.replace("(-","")
            binding.txtResult.text= expression.replace("(-","")
        }
        mainViewModel.calculationText= binding.txtCalculation.text.toString()
        mainViewModel.resultText= binding.txtResult.text.toString()
    }

    //Operator
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "SuspiciousIndentation", "ResourceAsColor")
    private fun defineOperators() {

        binding.apply {
            //%
            btnPercent.setOnClickListener {
             if (txtResult.text.toString().equals("0")) {
                    appendText("0%")
                }
                if (txtCalculation.text.last() != '+' && txtCalculation.text.last() != '-' &&
                    txtCalculation.text.last() != '×' && txtCalculation.text.last() != '÷'
                    && txtCalculation.text.last() != '%'
                ) {
                    appendText("%")
                }


            }

            btnDivide.setOnClickListener {
                if (txtResult.text.toString().equals("0")) {
                    appendText("0÷")
                }

                if (txtCalculation.text.last() != '+' &&txtCalculation.text.last() != '-' &&
                    txtCalculation.text.last() != '×' && txtCalculation.text.last() != '÷'
                ) {
                    appendText("÷")
                }
            }

            btnMultiply.setOnClickListener {
                if (txtResult.text.toString().equals("0")) {
                    appendText("0×")
                }

                if (txtCalculation.text.last() != '+' && txtCalculation.text.last() != '-' &&
                    txtCalculation.text.last() != '×' && txtCalculation.text.last() != '÷'
                ) {
                    appendText("×")
                }
            }

            btnSubtract?.setOnClickListener {
                if (txtResult.text.toString().equals("0")) {
                    appendText("0-")
                }
                if (txtCalculation.text.last() != '+' && txtCalculation.text.last() != '-' &&
                    txtCalculation.text.last() != '×' && txtCalculation.text.last() != '÷'
                ) {
                    appendText("-")
                }
            }

           btnPlus.setOnClickListener {
               if (txtResult.text.toString().equals("0")) {
                   appendText("0+")
               }
               if (txtCalculation.text.last() != '+' && txtCalculation.text.last() != '-' &&
                   txtCalculation.text.last() != '×' &&  txtCalculation.text.last() != '÷'
               ) {
                   appendText("+")
               }
            }

           btnEqual.setOnClickListener {
               calculateResult()
                val expression=txtCalculation.text.toString()
                val result=txtResult.text.toString()
                entity=CalcEntity(0,expression,result)
                historyViewModel.insertHistory(entity)
            }

            //dot click
           btnDot.setOnClickListener {
               val textAnswer = txtCalculation.text.toString()
               val x = txtCalculation.text
               if (textAnswer.equals("0")) {
                   appendText("0.")
               } else if (x.endsWith('+') || x.endsWith('-') || x.endsWith('×') || x.endsWith('÷')) {
                   appendText(".")
               } else if (!x.endsWith('.')) {
                   appendText(".")
               }
           }

          btnE?.setOnClickListener { appendText("e") }

            //History btn
          btnHistory.setOnClickListener {
                val intent=Intent(this@CalculatorActivity, HistoryActivity::class.java)
                startActivity(intent)
            }

            // toggle rad and deg
            btnRad?.setOnClickListener {
                if (btnRad.text.toString()=="Rad"){
                    btnRad.text="Deg"
                    btnRad.setBackgroundColor(getColor(R.color.yellow))
                }else{
                    btnRad.text="Rad"
                }
            }

            btnShift?.setOnClickListener {toggleShift()}
        }

    }

    @SuppressLint("SetTextI18n")
    private fun toggleShift(){
        binding.apply {
            if (btnSqrt?.text==getString(R.string.sqrt)) {
                btnSqrt.text = getString(R.string.cbrt)
                btnSqrt.setOnClickListener { appendText("cbrt(") }
            }
            else {
                btnSqrt?.text =getString(R.string.sqrt)
                btnSqrt?.setOnClickListener { appendText("sqrt(") }
            }

            if (btnSin?.text==getString(R.string.sin)) {
                btnSin.text = getString(R.string.asin)
                btnSin.setOnClickListener { appendText("asin(") }
            }
            else {
                btnSin?.text =getString(R.string.sin)
                btnSin?.setOnClickListener { appendText("sin(") }
            }

            if (btnCos?.text==getString(R.string.cos)) {
                btnCos.text = getString(R.string.acos)
                btnCos.setOnClickListener { appendText("acos(") }
            }
            else {
                btnCos?.text =getString(R.string.cos)
                btnCos?.setOnClickListener { appendText("cos(") }
            }

            if (btnTan?.text==getString(R.string.tan)) {
                btnTan.text = getString(R.string.atan)
                btnTan.setOnClickListener { appendText("atan(") }
            }
            else {
                btnTan?.text =getString(R.string.tan)
                btnTan?.setOnClickListener { appendText("tan(") }
            }

            if (btnLn?.text==getString(R.string.ln)) {
                btnLn.text = getString(R.string.sinh)
                btnLn.setOnClickListener { appendText("sinh(") }
            }
            else {
                btnLn?.text =getString(R.string.ln)
                btnLn?.setOnClickListener { appendText("ln(") }
            }

            if (btnLog?.text==getString(R.string.logarithm)) {
                btnLog.text = getString(R.string.cosh)
                btnLog.setOnClickListener { appendText("cosh(") }
            }
            else {
                btnLog?.text =getString(R.string.logarithm)
                btnLog?.setOnClickListener { appendText("log10(") }
            }

            if (btnDivideByOne?.text==getString(R.string._1_x)) {
                btnDivideByOne.text = getString(R.string.tanh)
                btnDivideByOne.setOnClickListener { appendText("tanh(") }
            }
            else {
                btnDivideByOne?.text =getString(R.string._1_x)
                btnDivideByOne?.setOnClickListener { appendText("1/") }
            }

            if (btnPow2?.text==getString(R.string.x2)) {
                btnPow2.text = getString(R.string.asinh)
                btnPow2.setOnClickListener { appendText("asinh(") }
            }
            else {
                btnPow2?.text =getString(R.string.x2)
                btnPow2?.setOnClickListener { appendText("^2") }
            }

            if (btnPow?.text==getString(R.string.x_y)) {
                btnPow.text = getString(R.string.acosh)
                btnPow.setOnClickListener { appendText("acosh(") }
            }
            else {
                btnPow?.text =getString(R.string.x_y)
                btnPow?.setOnClickListener { appendText("^") }
            }
            if (btnPercent.text==getString(R.string.percent)) {
                btnPercent.text = getString(R.string.atanh)
                btnPercent.setOnClickListener { appendText("atanh(") }
            }
            else {
                btnPercent.text =getString(R.string.percent)

            }

            if (btnAbs?.text  ==getString(R.string.absSign)) {
                btnAbs.text = getString(R.string.secondx)
                btnAbs.setOnClickListener { appendText("2^") }
            }
            else {
                btnAbs?.text  =getString(R.string.absSign)
                btnAbs?.setOnClickListener { appendText("abs(") }
            }

            if (btnPi?.text  ==getString(R.string.piSign)) {
                btnPi.text = getString(R.string.factSign)
                btnPi.setOnClickListener {
                    var hasFact=false
                    val txt=txtCalculation.text.toString()
                    if (txt.endsWith("!") && !hasFact){
                        hasFact=true
                    }else{
                        appendText("!")
                    }
                }
            }
            else {
                btnPi?.text  =getString(R.string.piSign)
                btnPi?.setOnClickListener { appendText("π") }
            }
            if (btnE?.text  ==getString(R.string.eSign)) {
                btnE.text = getString(R.string.ex)
                btnE.setOnClickListener { appendText("e^") }
            }
            else {
                btnE?.text  =getString(R.string.eSign)
                btnE?.setOnClickListener { appendText("e") }
            }
        }

    }

    //Append txt
    @SuppressLint("SetTextI18n")
    private fun appendText(text: String){
        mainViewModel.appendText(text)
        updateTextview()

        if (binding.txtCalculation.text.contains('+') && !binding.txtResult.text.contains('+')) {
            binding.txtCalculation.text = binding.txtResult.text.substring(1, binding.txtResult.text.length)
        } else if (binding.txtCalculation.text.contains('-') && !binding.txtResult.text.contains('-')) {
            binding.txtCalculation.text = binding.txtResult.text.substring(1, binding.txtResult.text.length)
        } else if (binding.txtCalculation.text.contains('÷') && !binding.txtResult.text.contains('÷')) {
            binding.txtCalculation.text = binding.txtResult.text.substring(1, binding.txtResult.text.length)
        } else if (binding.txtCalculation.text.contains('×') && !binding.txtResult.text.contains('×')) {
            binding.txtCalculation.text = binding.txtResult.text.substring(1, binding.txtResult.text.length)
        }else if (binding.txtCalculation.text.contains('%') && !binding.txtResult.text.contains('%')){
            binding.txtCalculation.text = binding.txtResult.text.substring(1, binding.txtResult.text.length)
        }
    }

    private fun updateTextview(){
        binding.txtCalculation.text=mainViewModel.calculationText
        binding.txtResult.text = mainViewModel.resultText
    }

    @SuppressLint("SetTextI18n")
    private fun splitExpression(){
        val expression = binding.txtCalculation.text.toString()

// Assuming the expression format is like "operand+operand%", split based on the '+' operator
        val digits = expression.split("+","%","-","÷","×")

   /*     digits.forEach {
            Log.e( "splitExpression: ",it )
        }*/

        val digit1=digits[0].toDouble()
        val digit2=digits[1].toDouble()
        Log.e( "digits:", "${digit1} ${digit2} ")

        val percentage=digit1+(digit1*digit2/100)
        Log.e( "percentage:", "${percentage} ")

        binding.txtResult.text=percentage.toString()
        mainViewModel.resultText=binding.txtResult.text.toString()

       // updateTextview()




        }

}