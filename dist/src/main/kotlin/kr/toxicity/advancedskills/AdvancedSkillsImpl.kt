package kr.toxicity.advancedskills

import kr.toxicity.advancedskills.api.AdvancedSkills
import kr.toxicity.advancedskills.api.event.PluginLoadEvent
import kr.toxicity.advancedskills.api.event.PluginReloadedEvent
import kr.toxicity.advancedskills.api.nms.NMS
import kr.toxicity.advancedskills.api.plugin.ReloadResult
import kr.toxicity.advancedskills.api.plugin.ReloadState
import kr.toxicity.advancedskills.api.scheduler.WrappedScheduler
import kr.toxicity.advancedskills.api.skill.SkillGenerator
import kr.toxicity.advancedskills.manager.*
import kr.toxicity.advancedskills.pack.PackData
import kr.toxicity.advancedskills.scheduler.FoliaScheduler
import kr.toxicity.advancedskills.scheduler.StandardScheduler
import kr.toxicity.advancedskills.skill.SkillGeneratorImpl
import kr.toxicity.advancedskills.util.*
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter
import java.util.function.Supplier

@Suppress("UNUSED")
class AdvancedSkillsImpl: AdvancedSkills() {

    private lateinit var managers: List<SkillsManager>
    private lateinit var nms: NMS
    private var onReload = false
    private lateinit var audiences: BukkitAudiences
    private var fileTreeWriter: Supplier<FileTreeWriter> = Supplier {
        FileTreeWriter.directory(DATA_FOLDER.subFolder("build"), true)
    }
    private lateinit var scheduler: WrappedScheduler
    private var namespace = "advancedskills"
    private val skillGenerator = SkillGeneratorImpl()

    override fun onEnable() {
        nms = when (val version = Bukkit.getServer()::class.java.`package`.name.split('.')[3]) {
            "v1_19_R3" -> kr.toxicity.advancedskills.nms.v1_19_R3.NMSImpl()
            "v1_20_R1" -> kr.toxicity.advancedskills.nms.v1_20_R1.NMSImpl()
            "v1_20_R2" -> kr.toxicity.advancedskills.nms.v1_20_R2.NMSImpl()
            "v1_20_R3" -> kr.toxicity.advancedskills.nms.v1_20_R3.NMSImpl()
            else -> {
                warn("Unsupported version found: $version")
                Bukkit.getPluginManager().disablePlugin(this)
                return
            }
        }
        scheduler = runCatching {
            Bukkit.getScheduler().runTaskAsynchronously(this@AdvancedSkillsImpl) { _ ->
                info("scheduler: standard")
            }
            StandardScheduler()
        }.getOrElse {
            FoliaScheduler().apply {
                asyncTask {
                    info("scheduler: folia")
                }
            }
        }
        managers = listOf(
            CompatibilityManager,
            SpaceManager,
            AnimationManager,
            EntityManager
        ).onEach {
            it.start()
        }
        audiences = BukkitAudiences.create(this)
        getCommand("advancedskills")?.setExecutor { sender, _, _, _ ->
            if (sender.hasPermission("advancedskills.reload")) {
                asyncTask {
                    val reload = reload()
                    when (reload.state) {
                        ReloadState.SUCCESS -> sender.sendMessage("Reload success! (${reload.time} ms)")
                        ReloadState.ON_RELOAD -> sender.sendMessage("This plugin is still on reload!")
                        ReloadState.FAIL -> sender.sendMessage("Reload failed! (${reload.time} ms)")
                    }
                }
            } else sender.sendMessage("You have no permission!")
            true
        }
        asyncTask {
            PluginLoadEvent().call()
            reload()
            info("Plugin enabled.")
        }
    }

    override fun onDisable() {
        managers.forEach {
            it.end()
        }
        audiences.close()
        info("Plugin disabled.")
    }

    override fun nms(): NMS = nms

    override fun reload(): ReloadResult {
        if (onReload) return ReloadResult(ReloadState.ON_RELOAD, 0)
        val time = System.currentTimeMillis()
        onReload = true
        return runCatching {
            managers.forEach {
                it.preReload()
            }
            val pack = PackData()
            managers.forEach {
                it.reload(pack)
            }
            managers.forEach {
                it.postReload()
            }
            MinecraftResourcePackWriter.minecraft().write(fileTreeWriter.get(), pack.resourcePack)
            val result = ReloadResult(ReloadState.SUCCESS, System.currentTimeMillis() - time)
            task {
                PluginReloadedEvent(result).call()
            }
            onReload = false
            result
        }.getOrElse { e ->
            warn("Failed to reload.")
            warn("Reason: ${e.message}")
            onReload = false
            ReloadResult(ReloadState.FAIL, System.currentTimeMillis() - time)
        }
    }

    override fun onReload(): Boolean = onReload
    override fun audiences(): BukkitAudiences = audiences
    override fun scheduler(): WrappedScheduler = scheduler
    override fun writerSupplier(): Supplier<FileTreeWriter> = fileTreeWriter
    override fun writerSupplier(writerSupplier: Supplier<FileTreeWriter>) {
        fileTreeWriter = writerSupplier
    }
    override fun namespace(): String = namespace
    override fun namespace(namespace: String) {
        this.namespace = namespace
    }
    override fun skillGenerator(): SkillGenerator = skillGenerator
}