package com.example.parking_tp3

import android.app.Person
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.example.tp3.R

class ParkingAdapter(var context: Fragment, var parkings: List<Parking>) :
    RecyclerView.Adapter<ParkingAdapter.ParkingHolder>() {
    class ParkingHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageParking: ImageView = itemView.findViewById(R.id.imageParking)
        val nomParking: TextView = itemView.findViewById(R.id.nomParking)
        val positionParking: TextView = itemView.findViewById(R.id.positionParking)
        val statutParking: TextView = itemView.findViewById(R.id.statutParking)
        val capaciteParking: TextView = itemView.findViewById(R.id.capaciteParking)
        val distanceParking: TextView = itemView.findViewById(R.id.distanceParking)
        val dureeParking: TextView = itemView.findViewById(R.id.dureeParking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_parking, parent, false)

        return ParkingHolder(view)
    }

    override fun onBindViewHolder(holder: ParkingHolder, position: Int) {
        val parking = parkings[position]
        holder.imageParking.setImageResource(parking.image)
        holder.nomParking.text = parking.nom
        holder.positionParking.text = parking.position
        holder.statutParking.text = parking.statut
        if (holder.statutParking.text == "Fermé") {
            holder.statutParking.setTextColor(Color.parseColor("#f00020"))
        } else {
            holder.statutParking.setTextColor(Color.parseColor("#008000"))
        }
        holder.capaciteParking.text = parking.capacite.plus(" % ")
        holder.distanceParking.text = parking.distance.plus(" Km ")
        holder.distanceParking.setTextColor(Color.parseColor("#0080ff"))
        holder.dureeParking.text = parking.duree.plus(" min ")
        holder.itemView.setOnClickListener(object:View.OnClickListener{
            override fun onClick(view: View?) {
                if (view != null) {
                    val bundle = bundleOf("position" to position)
                    view.findNavController().navigate(R.id.action_homeFragment_to_detailsParkingFragment,bundle)

                }
            }
        })
    }
    override fun getItemCount(): Int {
        return parkings.size
    }
}