package resilience4j.demo

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration

class Demo {

    fun setupRateLimiter(): RateLimiter {
        val config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .limitForPeriod(2)
            .build()

        val rateLimiterRegistry = RateLimiterRegistry.of(config)
        return rateLimiterRegistry.rateLimiter("someNameOfRateLimiter")
    }

    fun externalCallToGetExchangeRate(currencyPair: String) {
        val response = khttp.get(FOREX_API_BASE_URL, params = mapOf("pairs" to currencyPair)).text
        logger.info(currencyPair + " " + response.split("rate\":")[1].split(",")[0])
    }

    companion object {
        const val FOREX_API_BASE_URL = "https://www.freeforexapi.com/api/live"
        val logger: Logger = LoggerFactory.getLogger(Demo::class.java)
    }
}

fun main() {
    val demo = Demo()
    val myRateLimiter = demo.setupRateLimiter()
    val restrictedCall = RateLimiter.decorateConsumer<String>(
        myRateLimiter
    ) { currencyPair -> demo.externalCallToGetExchangeRate(currencyPair) }

    while (true) {
        restrictedCall.accept("EURUSD")
    }

}
