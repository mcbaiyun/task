package work.baiyun.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;
import java.util.Random;
public final class Task extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("task")).setExecutor(new TASKCmd());
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static class TASKCmd implements CommandExecutor{
        // This method is called, when somebody uses our command
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String lable, String[] args) {
            //判断sender是否为玩家，非玩家没有背包，无法给物品
            if (sender instanceof Player){
                Player player = (Player)sender;
                ItemStack handitem = player.getInventory().getItemInMainHand();
                if(handitem.getType().equals(Material.IRON_INGOT) && handitem.getAmount()>=24){
                    try {
                        if(isTaskFinished("TASK1",player.getName())){
                            sender.sendMessage("[Task]今日已完成任务1！");
                        }else{
                            SetTaskFinishTime("TASK1",player.getName());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.IRON_INGOT,handitem.getAmount()-24));
                            sender.sendMessage("[Task]任务1完成，奖励一颗绿宝石！");
                            player.getInventory().addItem(new ItemStack(Material.EMERALD,1));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (handitem.getType().equals(Material.GOLD_INGOT) && handitem.getAmount()>=36) {
                    try {
                        if(isTaskFinished("TASK2",player.getName())){
                            sender.sendMessage("[Task]今日已完成任务2！");
                        }else{
                            SetTaskFinishTime("TASK2",player.getName());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.GOLD_INGOT,handitem.getAmount()-36));
                            sender.sendMessage("[Task]任务2完成，奖励一颗绿宝石！");
                            player.getInventory().addItem(new ItemStack(Material.EMERALD,1));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else if (handitem.getType().equals(Material.PUMPKIN) && handitem.getAmount()>=4) {
                    try {
                        if(isTaskFinished("TASK3",player.getName())){
                            sender.sendMessage("[Task]今日已完成任务3！");
                        }else{
                            SetTaskFinishTime("TASK3",player.getName());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.PUMPKIN,handitem.getAmount()-4));
                            sender.sendMessage("[Task]任务3完成，奖励经验90！");
                            player.giveExp(90);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else if (handitem.getType().equals(Material.COAL) && handitem.getAmount()>=48) {
                    try {
                        if(isTaskFinished("TASK4",player.getName())){
                            sender.sendMessage("[Task]今日已完成任务4！");
                        }else{
                            SetTaskFinishTime("TASK4",player.getName());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.COAL,handitem.getAmount()-48));
                            sender.sendMessage("[Task]任务4完成，奖励一把铁剑 一把铁镐 两把金镐！");
                            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD,1));
                            player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE,1));
                            player.getInventory().addItem(new ItemStack(Material.GOLDEN_PICKAXE,2));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    sender.sendMessage("[Task]请手持【足够数量】的【规定的材料】再输入完成任务的指令");
                }


            }else {
                //当非玩家执行命令时，给个错误提示。
                sender.sendMessage("[Task]只有玩家才能完成委托！");
            }
            //当指令执行顺利时，返回true，否则玩家每次输入指令都会在客户端看到自己发送的指令。
            return true;
        }
    }
    public static boolean isTaskFinished(String name, String playername) throws IOException {
        long finishedtime =GetTaskFinishTime(name, playername);
        if(finishedtime == -1)
            return false;
        long ct = System.currentTimeMillis() / 1000 / 60/60/24;
        return ct - finishedtime <= 0;
    }
    public static long GetTaskFinishTime(String name, String playername) throws IOException {
        String dirpath = System.getProperty("java.io.tmpdir")+"baiyun\\";
        File dir = new File(dirpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        dirpath=dirpath+"task\\";
        dir = new File(dirpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        dirpath=dirpath+name+"\\";
        dir = new File(dirpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        File file = new File(dirpath+playername+".txt");
        int finishtime=0;
        String lineTxt = null;
        if(file.isFile() && file.exists()) { //判断文件是否存在
            String encoding = "GBK";
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);

            if ((lineTxt = bufferedReader.readLine()) != null) {
                return Long.parseLong(lineTxt);
            }
        }
        return System.currentTimeMillis() / 1000 / 60/60/24-1;
    }
    public static void SetTaskFinishTime(String name, String playername) throws IOException {
        String dirpath = System.getProperty("java.io.tmpdir")+"baiyun\\";
        File dir = new File(dirpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        dirpath=dirpath+"task\\";
        dir = new File(dirpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        dirpath=dirpath+name+"\\";
        dir = new File(dirpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        File file = new File(dirpath+playername+".txt");
        long ct = System.currentTimeMillis() / 1000 / 60/60/24;
        String t = String.valueOf(ct);
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter ow = new OutputStreamWriter(outputStream);
        ow.write(t);
        ow.flush();
    }


}
