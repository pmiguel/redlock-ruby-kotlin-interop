import org.redisson.Redisson
import org.redisson.config.Config
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val config = Config()
    config.useSingleServer().address = "redis://localhost:6379"

    val client = Redisson.create(config)

    println("Acquiring Lock...")
    val lock = client.getLock("lock_key2")
    val res = ml.tryLock(30, 60*10, TimeUnit.SECONDS)

    println("Lock acquire result $res")

    exitProcess(0)
}