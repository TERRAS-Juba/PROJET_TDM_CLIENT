package com.example.tp3

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Reservation
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class TestReservation {
    lateinit var mDataBase: AppBD

    @Before
    fun initDB() {
        mDataBase =
            Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                AppBD::class.java
            ).build()
    }

    @Test
    fun testAddReservation() {
        val r1: Reservation = Reservation(
            date = Date(),
            heure_entree = System.currentTimeMillis(),
            heure_sortie = System.currentTimeMillis(),
            etat = "En cours",
            code_qr = "123456",
            numero_place = 10
        )
        mDataBase.getReservationDao().insert(r1)
        val r2 = mDataBase.getReservationDao().getReservationByCodeQR("123456")
        assertEquals(r1, r2)
    }

    @Test
    fun testGetReservations() {
        mDataBase.getReservationDao().insert(
            Reservation(
                date = Date(),
                heure_entree = System.currentTimeMillis(),
                heure_sortie = System.currentTimeMillis(),
                etat = "En cours",
                code_qr = "998877",
                numero_place = 22
            )
        )
        assertEquals(mDataBase.getReservationDao().getReservations().size, 1)
    }

    @Test
    fun testGetReservationsEnCours() {
        mDataBase.getReservationDao().insert(
            Reservation(
                date = Date(),
                heure_entree = System.currentTimeMillis(),
                heure_sortie = System.currentTimeMillis(),
                etat = "En cours",
                code_qr = "998877",
                numero_place = 22
            )
        )
        assertEquals(mDataBase.getReservationDao().getReservationEnCours(System.currentTimeMillis()).size, 0)
        mDataBase.getReservationDao().insert(
            Reservation(
                date = Date(),
                heure_entree = System.currentTimeMillis(),
                heure_sortie = System.currentTimeMillis()+100000000000000,
                etat = "En cours",
                code_qr = "1000000",
                numero_place = 22
            )
        )
        assertEquals(mDataBase.getReservationDao().getReservationEnCours(System.currentTimeMillis()).size, 1)
    }

    @After
    fun closeDb() {
        mDataBase.close()
    }

}