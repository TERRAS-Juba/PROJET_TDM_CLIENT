package com.example.tp3

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tp3.databinding.FragmentDetailsReservationBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter


class DetailsReservationFragment : Fragment() {
    lateinit var Binding:FragmentDetailsReservationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Binding = FragmentDetailsReservationBinding.inflate(inflater, container, false)
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id_reservation= arguments?.getString("id_reservation")
        val numero_place=arguments?.getInt("numero_place")
        val numero:TextView=view.findViewById(R.id.numeroPlaceReservation)
        val id:TextView=view.findViewById(R.id.idReservation)
        id.text=id_reservation
        if(numero_place==0){
            numero.text=("Numero de place : ").plus("en attente")
        }else{
            numero.text=("Numero de place : ").plus(numero_place)
        }
        val writer=QRCodeWriter()
        try {
            val bitMatrix=writer.encode(id_reservation.toString(),BarcodeFormat.QR_CODE,512,512)
            val width=bitMatrix.width
            val height=bitMatrix.height
            val bmp=Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
            for(x in 0 until width){
                for(y in 0 until height){
                    bmp.setPixel(x,y,if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                }
            }
            val image:ImageView=view.findViewById(R.id.codeQR)
            image.setImageBitmap(bmp)
        }catch(e:WriterException){
            e.printStackTrace()
        }


    }
}