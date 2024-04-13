package lzy.lugin.firstplugin;

import lzy.lugin.firstplugin.command.GetEquipmentCommand;
import lzy.lugin.firstplugin.items.Equipment;
import lzy.lugin.firstplugin.myListener.CustomEquipment;
import lzy.lugin.firstplugin.myListener.DeathListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class FirstPlugin extends JavaPlugin {

    private File configFile;
    private FileConfiguration config;
    @Override
    public void onEnable() {
        // 检查插件文件夹是否存在，如果不存在则创建
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // 加载配置文件
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            // 如果配置文件不存在，就创建一个新的配置文件
            saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // 读取配置文件中的值，决定掉落的物品数量
        int dropItemCount = getConfig().getInt("dropItemCount", 5); // 默认为5

        // 注册事件监听器并传递掉落物品数量
        getServer().getPluginManager().registerEvents(new DeathListener(dropItemCount), this);
        getServer().getPluginManager().registerEvents(new CustomEquipment(), this);
        // 注册命令执行器
        getCommand("get").setExecutor(new GetEquipmentCommand(new Equipment(Equipment.Type.HELMET, 10.0f, 0.8f)));
        // 使用插件的日志记录器输出日志信息
        getLogger().info("插件启动!");
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // 保存配置文件
        saveConfig();
    }
    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            getLogger().warning("Could not save config to " + configFile);
        }
    }
}
