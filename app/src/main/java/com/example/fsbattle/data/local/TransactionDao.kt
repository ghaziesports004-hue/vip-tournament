package com.example.fsbattle.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fsbattle.data.models.CachedTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM cached_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<CachedTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: CachedTransaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<CachedTransaction>)

    @Query("DELETE FROM cached_transactions")
    suspend fun clearAll()
}
