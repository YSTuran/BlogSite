package yusufs.turan.blogsite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import yusufs.turan.blogsite.R
import yusufs.turan.blogsite.databinding.FragmentKullaniciBinding


class KullaniciFragment : Fragment() {
    private var _binding : FragmentKullaniciBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kayitButton.setOnClickListener { kaydol(it) }
        binding.girisButton.setOnClickListener { girisYap(it) }

        val aktifKullanici = auth.currentUser
        if (aktifKullanici != null){
            //daha önce giriş yapılmış
            val action =KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun kaydol(view: View){
        val email = binding.mailEditText.text.toString()
        val password = binding.sifreEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Kullanıcı Oluışturuldu
                    val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun girisYap(view: View){
        val email = binding.mailEditText.text.toString()
        val password = binding.sifreEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { task ->
                // Giriş Yapıldı
                val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}