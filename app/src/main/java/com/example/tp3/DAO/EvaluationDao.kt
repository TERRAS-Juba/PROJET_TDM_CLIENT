package com.example.tp3.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.tp3.Entites.Evaluation

@Dao
interface EvaluationDao:BaseDao<Evaluation>  {
    @Query("select * from Evaluation where synchronise=0")
    fun getEvaluationsNonSynchronise():List<Evaluation>
    @Query("UPDATE Evaluation set synchronise=1 where synchronise=0")
    fun synchEvaluation()
}