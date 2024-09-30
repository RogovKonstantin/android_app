package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.andr_dev_application.databinding.FragmentFirstFunctionBinding

class FirstFunctionFragment : BaseFragment<FragmentFirstFunctionBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFirstFunctionBinding {
        return FragmentFirstFunctionBinding.inflate(inflater, container, false)
    }
}