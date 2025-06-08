import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class CustomPopup: DialogFragment() {

    companion object {
        const val TAG = "CustomPopupView"

        fun newInstance(): CustomPopup {
            return CustomPopup()
        }
    }

    private lateinit var contentContainer: FrameLayout
    private val containerId = View.generateViewId()

    // 콜백 인터페이스
    interface PopupCallback {
        fun onDismiss() {}
        fun onShow() {}
    }

    private var callback: PopupCallback? = null
    private var isDismissible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 다이얼로그 스타일 설정
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // 다이얼로그 윈도우 설정
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.CENTER)

            // 상태바 투명 설정 (선택사항)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createPopupLayout()
    }

    private fun createPopupLayout(): View {
        val context = requireContext()

        // 루트 컨테이너 (전체 화면, 반투명 배경)
        val rootContainer = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#80000000"))

            // 배경 터치 시 닫기
            setOnClickListener {
                if (isDismissible) {
                    dismiss()
                }
            }
        }

        // 컨텐츠 컨테이너
        contentContainer = FrameLayout(context).apply {
            id = containerId
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            ).apply {
                setMargins(48, 48, 48, 48)
            }

            setBackgroundColor(Color.WHITE)
            elevation = 16f

            // 모서리 둥글게 (API 21+)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                outlineProvider = ViewOutlineProvider.BACKGROUND
                clipToOutline = true
            }

            // 컨텐츠 영역 터치 시 이벤트 소비
            setOnClickListener { /* 이벤트 소비 */ }
        }

        rootContainer.addView(contentContainer)

        return rootContainer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShow()
    }

    /**
     * Fragment를 팝업에 표시
     */
    fun setContentFragment(fragment: Fragment, tag: String? = null) {
        if (::contentContainer.isInitialized) {
            childFragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commit()
        }
    }

    /**
     * Fragment 교체 (애니메이션 포함)
     */
    fun replaceContentFragment(fragment: Fragment, tag: String? = null, addToBackStack: Boolean = false) {
        if (::contentContainer.isInitialized) {
            val transaction = childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                .replace(containerId, fragment, tag)

            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }

            transaction.commit()
        }
    }

    /**
     * 팝업 표시
     */
    fun show(fragmentManager: FragmentManager, tag: String = TAG) {
        if (!isAdded && !isVisible) {
            super.show(fragmentManager, tag)
        }
    }

    /**
     * 안전한 팝업 표시 (중복 방지)
     */
    fun showSafely(fragmentManager: FragmentManager, tag: String = TAG) {
        if (fragmentManager.isStateSaved) return

        val existingFragment = fragmentManager.findFragmentByTag(tag)
        if (existingFragment == null) {
            show(fragmentManager, tag)
        }
    }

    /**
     * 백 버튼 처리
     */
    fun onBackPressed(): Boolean {
        return if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            true
        } else {
            if (isDismissible) {
                dismiss()
            }
            isDismissible
        }
    }

    /**
     * 팝업 설정 메서드들
     */
    fun setDismissible(dismissible: Boolean): CustomPopup {
        isDismissible = dismissible
        isCancelable = dismissible
        return this
    }

    fun setCallback(callback: PopupCallback): CustomPopup {
        this.callback = callback
        return this
    }

    fun setContentSize(width: Int, height: Int): CustomPopup {
        if (::contentContainer.isInitialized) {
            contentContainer.layoutParams = FrameLayout.LayoutParams(width, height, Gravity.CENTER).apply {
                setMargins(48, 48, 48, 48)
            }
        }
        return this
    }

    fun setContentMargins(left: Int, top: Int, right: Int, bottom: Int): CustomPopup {
        if (::contentContainer.isInitialized) {
            (contentContainer.layoutParams as? FrameLayout.LayoutParams)?.setMargins(left, top, right, bottom)
        }
        return this
    }

    fun setContentBackground(color: Int): CustomPopup {
        if (::contentContainer.isInitialized) {
            contentContainer.setBackgroundColor(color)
        }
        return this
    }

    fun setBackgroundDimAmount(alpha: Float): CustomPopup {
        dialog?.window?.setDimAmount(alpha)
        return this
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        callback?.onDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback = null
    }
}

// 확장 함수들
fun CustomPopup.Builder() = CustomPopupViewBuilder()

class CustomPopupViewBuilder {
    private val popup = CustomPopup.newInstance()
    private var fragment: Fragment? = null
    private var fragmentTag: String? = null

    fun setContentFragment(fragment: Fragment, tag: String? = null): CustomPopupViewBuilder {
        this.fragment = fragment
        this.fragmentTag = tag
        return this
    }

    fun setDismissible(dismissible: Boolean): CustomPopupViewBuilder {
        popup.setDismissible(dismissible)
        return this
    }

    fun setCallback(callback: CustomPopup.PopupCallback): CustomPopupViewBuilder {
        popup.setCallback(callback)
        return this
    }

    fun setContentSize(width: Int, height: Int): CustomPopupViewBuilder {
        popup.setContentSize(width, height)
        return this
    }

    fun setContentMargins(left: Int, top: Int, right: Int, bottom: Int): CustomPopupViewBuilder {
        popup.setContentMargins(left, top, right, bottom)
        return this
    }

    fun setContentBackground(color: Int): CustomPopupViewBuilder {
        popup.setContentBackground(color)
        return this
    }

    fun setBackgroundDimAmount(alpha: Float): CustomPopupViewBuilder {
        popup.setBackgroundDimAmount(alpha)
        return this
    }

    fun build(): CustomPopup {
        fragment?.let {
            popup.setContentFragment(it, fragmentTag)
        }
        return popup
    }

    fun show(fragmentManager: FragmentManager, tag: String = CustomPopup.TAG) {
        build().showSafely(fragmentManager, tag)
    }
}