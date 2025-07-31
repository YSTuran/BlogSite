package yusufs.turan.blogsite.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
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
import yusufs.turan.blogsite.model.Gonderi

class FeedFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var popup: PopupMenu
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private val gonderiListesi = arrayListOf<Gonderi>()
    private lateinit var adapter: GonderiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GonderiAdapter(gonderiListesi)
        binding.gonderiRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.gonderiRecyclerView.adapter = adapter

        popup = PopupMenu(requireContext(), binding.floatingActionButton).apply {
            menuInflater.inflate(R.menu.popup_menu, menu)
            setOnMenuItemClickListener(this@FeedFragment)
        }

        binding.floatingActionButton.setOnClickListener {
            popup.show()
        }

        firestoreVeriAl()
    }

    private fun firestoreVeriAl() {
        db.collection("Gonderi")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                } else if (value != null) {
                    gonderiListesi.clear()
                    for (document in value.documents) {
                        val baslik = document.getString("Baslik") ?: ""
                        val metin = document.getString("Metin") ?: ""
                        val email = document.getString("Email") ?: ""
                        val gorselUrl = document.getString("gorselUrl") ?: ""
                        gonderiListesi.add(Gonderi(email, baslik, metin, gorselUrl))
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.yuklemeItem -> {
                val action = FeedFragmentDirections.actionFeedFragmentToGonderiFragment()
                Navigation.findNavController(requireView()).navigate(action)
                true
            }
            R.id.cikisItem -> {
                auth.signOut()
                val action = FeedFragmentDirections.actionFeedFragmentToKullaniciFragment()
                Navigation.findNavController(requireView()).navigate(action)
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
