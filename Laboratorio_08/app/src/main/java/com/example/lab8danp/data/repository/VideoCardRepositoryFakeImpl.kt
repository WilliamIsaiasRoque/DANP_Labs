package com.example.lab8danp.data.repository

import com.example.lab8danp.R
import com.example.lab8danp.domain.model.VideoCard
import com.example.lab8danp.domain.repository.VideoCardRepository
import javax.inject.Inject

class VideoCardRepositoryFakeImpl @Inject constructor() : VideoCardRepository {

    // Precios actualizados a Soles (S/)
    private val videoCards = listOf(
        VideoCard(1, "RTX 4090", "NVIDIA", "24GB GDDR6X", 7500.00, R.drawable.rtx_4090),
        VideoCard(2, "RTX 4070 Ti", "NVIDIA", "12GB GDDR6X", 3800.00, R.drawable.rtx_4070_ti),
        VideoCard(3, "RTX 3080", "NVIDIA", "10GB GDDR6X", 3200.00, R.drawable.rtx_3080),
        VideoCard(4, "RTX 3060", "NVIDIA", "12GB GDDR6", 1450.00, R.drawable.rtx_3060),
        VideoCard(5, "GTX 1660 Super", "NVIDIA", "6GB GDDR6", 950.00, R.drawable.gtx_1660_super),
        VideoCard(6, "RX 7900 XTX", "AMD", "24GB GDDR6", 4800.00, R.drawable.rx_7900_xtx),
        VideoCard(7, "RX 7800 XT", "AMD", "16GB GDDR6", 2400.00, R.drawable.rx_7800_xt),
        VideoCard(8, "RX 6700 XT", "AMD", "12GB GDDR6", 1650.00, R.drawable.rx_6700_xt),
        VideoCard(9, "RX 6600", "AMD", "8GB GDDR6", 1050.00, R.drawable.rx_6600),
        VideoCard(10, "Arc A770", "Intel", "16GB GDDR6", 1350.00, R.drawable.arc_a770)
    )

    override fun getVideoCards(): List<VideoCard> = videoCards

    override fun getVideoCardById(id: Int): VideoCard? = videoCards.find { it.id == id }
}