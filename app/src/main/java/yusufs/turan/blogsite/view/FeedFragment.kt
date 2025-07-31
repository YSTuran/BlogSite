package yusufs.turan.blogsite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import yusufs.turan.blogsite.R
import yusufs.turan.blogsite.adapter.GonderiAdapter
import yusufs.turan.blogsite.databinding.FragmentFeedBinding
import yusufs.turan.blogsite.databinding.FragmentKullaniciBinding
import yusufs.turan.blogsite.model.Gonderi


class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var popup: PopupMenu
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    val gonderiListesi: ArrayList<Gonderi> = arrayListOf()
    private var adapter: GonderiAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener { floatingButtonTiklandi(it) }

        popup = PopupMenu(requireContext(), binding.floatingActionButton)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_menu, popup.menu)
        popup.setOnMenuItemClickListener(this)

        firestoreVeriAl()

        adapter = GonderiAdapter(gonderiListesi)
        binding.gonderiRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.gonderiRecyclerView.adapter = adapter
    }


    fun floatingButtonTiklandi(view: View) {
        popup.show()
    }

    private fun firestoreVeriAl() {
        db.collection("Gonderi").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if (value != null){
                    gonderiListesi.clear()
                    val documents = value.documents
                    for (document in documents){
                        val baslik = document.get("Baslik") as String
                        val metin = document.get("Metin") as String
                        val email = document.get("Email") as String
                        val gorselUrl = document.get("gorselUrl") as String

                        val gonderi = Gonderi(email, baslik, metin, gorselUrl)
                        gonderiListesi.add(gonderi)
                    }
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.yuklemeItem) {
            val action = FeedFragmentDirections.actionFeedFragmentToGonderiFragment()
            Navigation.findNavController(requireView()).navigate(action)
        } else if (item?.itemId == R.id.cikisItem) {
            //Çıkış Yapma İşlemi
            auth.signOut()
            val action = FeedFragmentDirections.actionFeedFragmentToKullaniciFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        return true
    }


}