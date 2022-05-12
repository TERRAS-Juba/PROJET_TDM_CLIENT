package com.example.tp3.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tp3.Entites.Parking
import com.example.tp3.R

class ParkingAdapter(var context: FragmentActivity) :
    RecyclerView.Adapter<ParkingAdapter.ParkingHolder>() {
    var data = mutableListOf<Parking>()

    fun setParkings(parkings: List<Parking>) {
        this.data = parkings.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_parking, parent, false)

        return ParkingHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ParkingHolder, position: Int) {
        bind(holder, data[position])
    }

    private fun bind(holder: ParkingHolder, parking: Parking) {
        holder.apply {
            holder.nomParking.text = parking.nom
            if (!parking.etat) {
                holder.etatParking.text="Ferme"
                holder.etatParking.setTextColor(Color.parseColor("#f00020"))
            } else {
                holder.etatParking.text="Ouvert"
                holder.etatParking.setTextColor(Color.parseColor("#008000"))
            }
            holder.remplissageParking.text="15 %"
            holder.communeParking.text=parking.commune
            holder.distanceParking.text = "15 Km "
            holder.distanceParking.setTextColor(Color.parseColor("#4287f5"))
            holder.dureeParking.text="15 min"
            Glide.with(context).load(parking.photo).apply(RequestOptions().error(R.drawable.ic_baseline_error_outline_24)).diskCacheStrategy(
                DiskCacheStrategy.ALL).into(holder.imageParking)
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    if (view != null) {
                        val bundle = bundleOf("position" to position,
                            "remplissageParking" to holder.remplissageParking.text,
                            "dureeParking" to holder.dureeParking.text,
                            "distanceParking" to holder.distanceParking.text
                        )
                        view.findNavController()
                            .navigate(R.id.action_homeFragment_to_detailsParkingFragment, bundle)

                    }
                }
            })
        }

    }

    class ParkingHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nomParking: TextView = itemView.findViewById(R.id.nomParking)
        val etatParking:TextView=itemView.findViewById(R.id.etatParking)
        val remplissageParking:TextView=itemView.findViewById(R.id.remplissageParking)
        val communeParking:TextView=itemView.findViewById(R.id.communeParking)
        val distanceParking:TextView=itemView.findViewById(R.id.distanceParking)
        val dureeParking:TextView=itemView.findViewById(R.id.dureeParking)
        val imageParking: ImageView = itemView.findViewById(R.id.imageParking)
    }
}