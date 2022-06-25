package com.example.tp3

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Parking
import com.example.tp3.Entites.Reservation
import com.example.tp3.ViewModels.ReservationViewModel
import com.example.tp3.ViewModels.UtilisateurViewModel
import com.example.tp3.databinding.FragmentAjouterReservationBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AjouterReservationFragment : Fragment() {
    lateinit var parkingViewModel: ParkingViewModel
    lateinit var Binding: FragmentAjouterReservationBinding
    lateinit var reservationViewModel: ReservationViewModel
    lateinit var utilisateurViewModel: UtilisateurViewModel
    var dialog: AlertDialog? = null
    lateinit var parkingReservation: Parking
    var cal = Calendar.getInstance()
    var prixReservation: Double = 0.0
    var reservatinMap = HashMap<String, String>()
    var paiementMap = HashMap<String, String>()
    val dateFormat = SimpleDateFormat("yyyy.MM.dd")
    var listeReservation = mutableListOf<Reservation>()
    lateinit var reservation: Reservation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Binding = FragmentAjouterReservationBinding.inflate(inflater, container, false)
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Binding.heureEntreeFormulaire.setIs24HourView(true)
        Binding.heureSortieFormulaire.setIs24HourView(true)
        parkingViewModel = ViewModelProvider(requireActivity()).get(ParkingViewModel::class.java)
        utilisateurViewModel =
            ViewModelProvider(requireActivity()).get(UtilisateurViewModel::class.java)
        reservationViewModel =
            ViewModelProvider(requireActivity()).get(ReservationViewModel::class.java)
        reservationViewModel.nbPlaces.value = null
        reservationViewModel.paye.value = null
        reservationViewModel.effectue.value = null
        reservationViewModel.errorMessage.value = null
        val position = arguments?.getInt("position")
        if (position != null) {
            parkingReservation = parkingViewModel.parkings.value?.get(position)!!
            Binding.nomParkingFormulaire.text = parkingReservation.nom
            Binding.nomParkingFormulaire.setTextColor(Color.parseColor("#008000"))
        }
        Binding.ButtonReservation.setOnClickListener {
            val datePicker: DatePicker = Binding.dateReservationFormulaire
            val year: Int = datePicker.year
            val month: Int = datePicker.month
            val day: Int = datePicker.dayOfMonth
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)
            val dateInMiliSecondes = cal.timeInMillis
            Log.d("Date", dateInMiliSecondes.toString())
            val timePickerEntree: TimePicker = Binding.heureEntreeFormulaire
            cal.set(Calendar.HOUR, timePickerEntree.hour)
            cal.set(Calendar.MINUTE, timePickerEntree.minute)
            val heureEntreeInMiliSecondes = cal.timeInMillis
            val timePickerSortie: TimePicker = Binding.heureSortieFormulaire
            cal.set(Calendar.HOUR, timePickerSortie.hour)
            cal.set(Calendar.MINUTE, timePickerSortie.minute)
            val heureSortieInMiliSecondes = cal.timeInMillis
            val pref = requireActivity().getSharedPreferences("db_privee", Context.MODE_PRIVATE)
            reservation = Reservation(
                date_reservation = Date(dateInMiliSecondes),
                heure_entree = heureEntreeInMiliSecondes.toDouble(),
                heure_sortie = heureSortieInMiliSecondes.toDouble(),
                etat = true,
                numero_place = null,
                id_parking = parkingReservation.id_parking,
                id_utilisateur = pref.getString("id_utilisateur", "")!!.toInt(),
                id_paiement = "",
                synchronise = false
            )
            reservatinMap["date_reservation"] =
                dateFormat.format(Date(dateInMiliSecondes)).toString()
            reservatinMap["heure_entree"] = heureEntreeInMiliSecondes.toString()
            reservatinMap["heure_sortie"] = heureSortieInMiliSecondes.toString()
            reservatinMap["id_parking"] = parkingReservation.id_parking.toString()
            reservationViewModel.getNombrePlacesOccupees(reservatinMap)
        }
        reservationViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading == true) {
                setProgressDialog("Operation en cours ...")
            } else {
                dialog?.cancel()
            }
        })
        reservationViewModel.nbPlaces.observe(viewLifecycleOwner, Observer { nbOccupees ->
            if (nbOccupees != null) {
                var nb = nbOccupees.size
                nb = parkingReservation.capacite - nb
                if (nb > 0) {
                    prixReservation =
                        (reservatinMap["heure_sortie"]!!.toLong() - reservatinMap["heure_entree"]!!.toLong()) * (parkingReservation.tarif / 60 / 60 / 1000)
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("Paiement de la reservation")
                    builder.setMessage("Le montant du paiement: $prixReservation DA")
                    builder.setIcon(android.R.drawable.ic_dialog_info)
                    builder.setNegativeButton("Payer") { dialogInterface, which ->
                        val pref = requireActivity().getSharedPreferences(
                            "db_privee",
                            Context.MODE_PRIVATE
                        )
                        paiementMap["id_paiement"] = pref.getString("id_utilisateur", "")!!
                            .plus(System.currentTimeMillis().toString())
                        paiementMap["montant"] = prixReservation.toString()
                        paiementMap["date_paiement"] =
                            dateFormat.format(Date(System.currentTimeMillis())).toString()
                        reservationViewModel.addPaiement(paiementMap)
                    }
                    builder.setNeutralButton("Annuler") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                } else {
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("Statut de reservation")
                    builder.setMessage("Aucune place n'est disponible pour le moment")
                    builder.setIcon(android.R.drawable.ic_dialog_info)
                    builder.setNeutralButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
        })
        reservationViewModel.paye.observe(viewLifecycleOwner, Observer { paye ->
            if (paye != null) {
                if (paye == true) {
                    reservation.id_paiement = paiementMap["id_paiement"].toString()
                    listeReservation.add(reservation)
                    reservationViewModel.ajouterReservation(listeReservation)
                    val bd: AppBD? = AppBD.buildDatabase(requireContext())
                    bd?.getReservationDao()?.insert(reservation)
                } else {
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("Statut de Paiement")
                    builder.setMessage("Le paiement a echoué")
                    builder.setIcon(android.R.drawable.ic_dialog_info)
                    builder.setNeutralButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
        })
        reservationViewModel.effectue.observe(viewLifecycleOwner, Observer { effectue ->
            if (effectue != null) {
                if (effectue == true) {
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("Statut de reservation")
                    builder.setMessage("Votre reservation a été effectuée avec succés")
                    builder.setIcon(android.R.drawable.ic_dialog_info)
                    builder.setNeutralButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                } else {
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("Statut de reservation")
                    builder.setMessage("Votre demande de reservation a echouée")
                    builder.setIcon(android.R.drawable.ic_dialog_info)
                    builder.setNeutralButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
        })
// Error message observer
        reservationViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if (message != null) {
                Toast.makeText(requireContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT)
                    .show()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setProgressDialog(message: String) {
// Creating a Linear Layout
        val llPadding = 30
        val ll = LinearLayout(requireActivity())
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
// Creating a ProgressBar inside the layout
        val progressBar = ProgressBar(requireActivity())
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
// Creating a TextView inside the layout
        val tvText = TextView(requireActivity())
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
// Setting the AlertDialog Builder view
// as the Linear layout created abovep
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(true)
        builder.setView(ll)
// Displaying the dialog
        dialog = builder.create()
        dialog?.show()
        val window: Window? = dialog?.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog?.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog?.window?.attributes = layoutParams
// Disabling screen touch to avoid exiting the Dialog
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }
}