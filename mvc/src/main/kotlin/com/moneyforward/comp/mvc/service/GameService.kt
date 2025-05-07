package com.moneyforward.comp.mvc.service

import com.moneyforward.comp.mvc.client.GameClient
import com.moneyforward.comp.mvc.config.DebugHostContext
import com.moneyforward.comp.shared.data.GameResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.slf4j.MDC
import org.springframework.stereotype.Service

@Service
class GameService(
    private val gameClient: GameClient,
    private val gameMetricService: GameMetricService,
) {
    fun getLatestGames(count: Int): List<GameResponse?> {
        val host = DebugHostContext.host
        val context = MDC.getCopyOfContextMap()
        val games = runBlocking {
            withContext(Dispatchers.IO) {
                (0..<count).map {
                    async {
                        MDC.setContextMap(context)
                        gameClient.getLatestGame(host)
                    }
                }.awaitAll()
            }
        }

        // made-up service requirement is to do this after we get all games
        gameMetricService.updateOrCreateMetrics(games.mapNotNull { it })
        return games
    }
}
