package ru.sergeyzavyalov.testapplication.custom_views

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.sergeyzavyalov.testapplication.R
import ru.sergeyzavyalov.testapplication.extensions.dp

class OtpCodeEditText constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {

    private var fieldsContainer: LinearLayout
    private var errorTextView: TextView

    /**
     * Длина кода
     */
    private var otpCodeLength: Int = 0

    /**
     * Background для айтемов
     */
    private var otpCodeItemBackground: Int = 0
    private var otpCodeItemErrorBackground: Int = 0
    private val fields = mutableListOf<EditText>()

    /**
     * Параметры для TextView ошибки
     */
    @DimenRes
    private var errorIcon: Int = 0

    @Dimension
    private var errorIconPadding: Int = 6
    private var _errorText: String? = null
    var errorText: String? = null
    set(value) {
        field = value
        errorTextView.text = value
        invalidate()
    }

    private var _isShowError: Boolean = false
    var isShowError: Boolean = false
    set(value) {
        field = value
        errorTextView.isVisible = value
        invalidate()
    }

    /**
     * Настройки текста внутри айтемов
     */
    @Dimension
    private var textSize: Int = 0
    private var textColor: Int = 0

    /**
     * Расстояние между айтемами во вью
     */
    @Dimension
    private var distanceBetween: Int = 0

    /**
     * Внешние отступы, нужны для теней при наличии [innerElevation].
     * Для внутренних нужно использовать параметр [distanceBetween]
     */
    @Dimension
    private var outerMargins: Int = 0

    /**
     * Внутренняя элевация каждого айтема отдельно.
     */
    @Dimension
    private var innerElevation: Int = 0

    /**
     * Полученный код для передачи во фрагмент/активити
     */
    private val _code = MutableLiveData<String>()
    val code: LiveData<String> = _code

    companion object {
        private const val DEFAULT_CODE_LENGTH = 4
        private const val DEFAULT_DISTANCE = 8
        private const val DEFAULT_ELEVATION = 6

        // Нужны для теней
        private const val DEFAULT_MARGINS = 8

        private const val EVENT_DOWN = KeyEvent.ACTION_DOWN
        private const val BACKSPACE_EVENT = KeyEvent.KEYCODE_DEL
        private val digitsEvents = listOf(
            KeyEvent.KEYCODE_0,
            KeyEvent.KEYCODE_1,
            KeyEvent.KEYCODE_2,
            KeyEvent.KEYCODE_3,
            KeyEvent.KEYCODE_4,
            KeyEvent.KEYCODE_5,
            KeyEvent.KEYCODE_6,
            KeyEvent.KEYCODE_7,
            KeyEvent.KEYCODE_8,
            KeyEvent.KEYCODE_9
        )
    }

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        initAttrs(attrs)
        fieldsContainer = createFieldsContainer()
        errorTextView = createErrorTextView()
        createCodeFields(fieldsContainer)
        addViewsToRoot()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        errorText = _errorText
        isShowError = _isShowError

        setFocusToFirstItem()
        setupOnKeyListener(currentEt = fields[0], nextEt = fields[1])
        initListeners()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val attrSet = context.theme.obtainStyledAttributes(
                it, R.styleable.OtpCodeEditText, 0, 0
            )

            otpCodeLength = attrSet.getInt(
                R.styleable.OtpCodeEditText_codeLength, DEFAULT_CODE_LENGTH
            )
            otpCodeItemBackground = attrSet.getResourceId(
                R.styleable.OtpCodeEditText_codeBackground, 0
            )
            otpCodeItemErrorBackground = attrSet.getResourceId(
                R.styleable.OtpCodeEditText_codeErrorStateBackground, otpCodeItemBackground
            )

            textSize = attrSet.getDimensionPixelSize(R.styleable.OtpCodeEditText_textSize, 24)
            textColor = attrSet.getColor(R.styleable.OtpCodeEditText_textColor, 0)
            distanceBetween = attrSet.getDimensionPixelSize(
                R.styleable.OtpCodeEditText_distanceBetween,
                DEFAULT_DISTANCE
            )
            errorIcon = attrSet.getResourceId(R.styleable.OtpCodeEditText_errorIconDrawable, 0)
            _errorText = attrSet.getString(R.styleable.OtpCodeEditText_errorText)
            _isShowError = attrSet.getBoolean(R.styleable.OtpCodeEditText_showError, false)

            innerElevation = attrSet.getDimensionPixelSize(
                R.styleable.OtpCodeEditText_innerElevation, DEFAULT_ELEVATION
            )
            outerMargins = attrSet.getDimensionPixelSize(
                R.styleable.OtpCodeEditText_outerMargins, DEFAULT_MARGINS
            )

            attrSet.recycle()
        }
    }

    private fun createFieldsContainer(): LinearLayout {
        val container = LinearLayout(context)
        container.orientation = HORIZONTAL
        val lp = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.gravity = Gravity.CENTER_HORIZONTAL
        container.layoutParams = lp
        return container
    }

    private fun createCodeFields(container: LinearLayout) {
        for (i in 0 until otpCodeLength) {
            val view = EditText(context)
            val params =
                LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0F
                )

            params.setMargins(outerMargins.dp)
            when (i) {
                0 -> params.rightMargin = distanceBetween
                otpCodeLength - 1 -> params.leftMargin = 0
                else -> params.setMargins(0, outerMargins.dp, distanceBetween, outerMargins.dp)
            }

            view.setBackgroundResource(otpCodeItemBackground)
            view.layoutParams = params
            view.elevation = innerElevation.toFloat()
            view.textSize = textSize.toFloat()
            view.setTextColor(textColor)
            view.gravity = Gravity.CENTER
            view.inputType = InputType.TYPE_CLASS_NUMBER
            view.limitLength(1)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                view.textCursorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_cursor)
            }

            container.addView(view)
            fields.add(view)
        }
    }

    private fun createErrorTextView(): TextView {
        val view = TextView(context)
        val lp = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.leftMargin = 2.dp
        lp.topMargin = 8.dp

        view.layoutParams = lp
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)

        view.compoundDrawablePadding = errorIconPadding.dp
        view.text = _errorText

        return view
    }

    private fun addViewsToRoot() {
        this.addView(fieldsContainer)
        this.addView(errorTextView)
    }

    private fun EditText.limitLength(maxLength: Int) {
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

    private fun setFocusToFirstItem() {
        fields[0].requestFocus()
        fields[0].isCursorVisible = true

        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun initListeners() {
        for (i in 0 until otpCodeLength) {
            when (i) {
                0 -> {
                    setupOnKeyListener(
                        currentEt = fields[i],
                        nextEt = fields[i + 1]
                    )

                    setupTextChangedListener(currentEt = fields[i], nextEt = fields[i + 1])
                }
                otpCodeLength - 1 -> {
                    setupOnKeyListener(
                        previousEt = fields[i - 1],
                        currentEt = fields[i]
                    )

                    setupTextChangedListener(currentEt = fields[i])
                }
                else -> {
                    setupOnKeyListener(
                        previousEt = fields[i - 1],
                        currentEt = fields[i],
                        nextEt = fields[i + 1]
                    )

                    setupTextChangedListener(currentEt = fields[i], nextEt = fields[i + 1])
                }
            }
        }
    }

    private fun setupTextChangedListener(currentEt: EditText, nextEt: EditText? = null) {
        currentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                if (text?.length == 1) {
                    setSelectionInNextEditText(nextEt)
                    isShowError = false
                }
                combineFieldsValuesToCode()
            }
        })
    }

    private fun setupOnKeyListener(
        previousEt: EditText? = null,
        currentEt: EditText,
        nextEt: EditText? = null,
    ) {
        currentEt.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == EVENT_DOWN) {
                if (keyCode in digitsEvents) {
                    if (currentEt.text.isNotEmpty()) {
                        nextEt?.let {
                            currentEt.clearFocus()
                            it.setValueAndFocus(keyCode)
                        }
                    }
                }
                if (keyCode == BACKSPACE_EVENT) {
                    if (currentEt.text.isBlank()) {
                        previousEt?.let {
                            currentEt.clearFocus()
                            it.setFocus()
                        }
                    }
                }
            }

            return@setOnKeyListener false
        }
    }

    private fun setSelectionInNextEditText(editText: EditText?) {
        editText?.requestFocus()
        editText?.isCursorVisible = true
    }

    private fun EditText.setValueAndFocus(value: Int) {
        this.requestFocus()
        this.isCursorVisible = true
        this.setText("${value - 7}", TextView.BufferType.EDITABLE)
        this.setSelection(this.text.length)
    }

    private fun EditText.setFocus() {
        this.requestFocus()
        this.isCursorVisible = true
        this.setSelection(this.text.length)
    }

    private fun combineFieldsValuesToCode() {
        val sb = StringBuffer()

        fields.forEach {
            sb.append(it.text.toString())
        }

        _code.value = sb.toString()
    }

    /**
     * Установка кода, пришедшего извне, например, из Push-уведомления
     */
    fun setCode(code: String) {
        for (i in code.indices) {
            fields[i].setText(code[i].toString(), TextView.BufferType.EDITABLE)
        }
        combineFieldsValuesToCode()

        invalidate()
    }

    fun isEnabled(isEnabled: Boolean) {
        fields.forEach {
            it.isEnabled = isEnabled
        }
        invalidate()
    }

    fun clearInput() {
        fields.forEach {
            it.text.clear()
        }

        setFocusToFirstItem()
    }
}