package com.example.tp3.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tp3.Entites.Parking
import com.example.tp3.Entites.Reservation
import com.example.tp3.R
import java.text.SimpleDateFormat
import java.util.*

class ReservationAdapter(var context: FragmentActivity) :
    RecyclerView.Adapter<ReservationAdapter.ReservationHolder>() {
    var data = mutableListOf<Reservation>()
    var dataparkings= mutableListOf<Parking>()
    fun setReservation(reservations: List<Reservation>) {
        this.data = reservations.toMutableList()
        notifyDataSetChanged()
    }
    fun setParkings(parkings: List<Parking>) {
        this.dataparkings= parkings.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservationAdapter.ReservationHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_reservation, parent, false)
        return ReservationHolder(view)
    }
    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(holder:ReservationHolder, position: Int) {
        bind(holder, data[position])
    }
    private fun bind(holder: ReservationAdapter.ReservationHolder, reservation: Reservation) {
        var parking: Parking? =null
        val simpleDateFormat = SimpleDateFormat("hh:mm:ss")
        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        holder.apply {
            for(item in dataparkings){
                if(item.id_parking==reservation.id_parking){
                    parking=item
                }
            }
            if(parking!=null){
                holder.nomParkingReservation.text = parking!!.nom
                holder.communeParkingReservation.text= parking!!.commune
                holder.tarifParkingReservation.text=parking!!.tarif.toString().plus(" DA/H")
                holder.tarifParkingReservation.setTextColor(Color.parseColor("#008000"))
            }else{
                holder.nomParkingReservation.text = "Impossible de se connecter a internet"
                holder.communeParkingReservation.text= "Impossible de se connecter a internet"
                holder.tarifParkingReservation.text="Impossible de se connecter a internet"
            }
            holder.heureEntreeReservation.text="Heure entree: ".plus(simpleDateFormat.format(Date(reservation.heure_entree.toLong())))
            holder.heureSortieReservation.text="Heure sortie: ".plus(simpleDateFormat.format(Date(reservation.heure_sortie.toLong())))
            holder.dateReservation.text="Date reservation: ".plus(dateFormat.format(reservation.date_reservation))
            holder.etatReservation.text="en cours"
            holder.etatReservation.setTextColor(Color.parseColor("#008000"))

        }
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (view != null) {
                    val bundle = bundleOf(
                        "id_reservation" to reservation.id_reservation,
                        "numero_place" to reservation.numero_place,
                    )
                    view.findNavController()
                        .navigate(R.id.action_mesReservationFragment_to_detailsReservationFragment, bundle)

                }
            }
        })
    }
    class ReservationHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nomParkingReservation: TextView = itemView.findViewById(R.id.nomParkingReservation)
        val communeParkingReservation: TextView =
            itemView.findViewById(R.id.communeParkingReservation)
        val tarifParkingReservation: TextView =
        itemView.findViewById(R.id.tarifParkingReservation)
        val heureEntreeReservation: TextView = itemView.findViewById(R.id.heureEntreeReservation)
        val heureSortieReservation: TextView = itemView.findViewById(R.id.heureSortieReservation)
        val dateReservation: TextView = itemView.findViewById(R.id.dateReservation)
        val etatReservation: TextView = itemView.findViewById(R.id.etatReservation)
    }
}