package com.alele.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(airport: Airport)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(airport: Airport)

    @Delete
    suspend fun delete(airport: Airport)

    @Query("SELECT * from airport ORDER BY passengers DESC")
    fun getAllAirport(): Flow<List<Airport>?>

    @Query("SELECT * from airport WHERE id = :id")
    fun getAirport(id: Int): Flow<Airport?>

    @Query("SELECT * from airport WHERE name like '%'||:name||'%' OR iata_code like '%'||:name||'%' ORDER BY passengers DESC")
    fun getAirportByName(name: String): Flow<List<Airport>?>
}