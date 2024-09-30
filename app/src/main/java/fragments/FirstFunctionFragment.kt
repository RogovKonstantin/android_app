package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.andr_dev_application.R

class FirstFunctionFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_first_function
    }

    companion object {
        fun newInstance(): FirstFunctionFragment {
            return FirstFunctionFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}