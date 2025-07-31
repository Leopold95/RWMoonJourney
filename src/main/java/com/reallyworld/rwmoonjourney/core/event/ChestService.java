package com.reallyworld.rwmoonjourney.core.event;

import com.reallyworld.rwmoonjourney.configs.ChestsConfig;
import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.models.ChestInfoModel;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@Slf4j
public class ChestService {
    private final Logger logger;

    public ChestService(Logger logger){
        this.logger = logger;
    }

    private final int MAX_SLOT_INDEX = 27;

    public void respawnAll() {
        removePrevious();

        var chests = chestsToSpawn();
        for (var chest: chests) {
            var location = chest.getLocation();
            var block = location.getBlock();
            block.setType(Material.CHEST);

            BlockFace direction = yawToCardinal(location.getYaw());

            if (block.getBlockData() instanceof Directional) {
                Directional directional = (Directional) block.getBlockData();
                directional.setFacing(direction);
                block.setBlockData(directional);
            }


            //TODO вынести в метод
            var chestState = (Chest) block.getState();
            var chestLoot = generateLoot(chest.getRarityCost()); //это надо заменить на кастомный генератор лута
            fillChestRandom(chestState, chestLoot);
        }
    }

    public Set<ItemStack> generateLoot(int rarityCost){
        var items = new HashSet<ItemStack>();

        items.add(new ItemStack(Material.DIAMOND, 2));
        items.add(new ItemStack(Material.DIAMOND_SWORD, 4));
        items.add(new ItemStack(Material.DIRT, 64));

        return items;
    }

    /**
     * Добавляет новый сундук который может быть заспавнен на арене.
     * @param rarity редкость сундука. Ограничего в конфиге
     * @param player позиция. Куда смотрит камера - туда будет смотреть замок сундука.
     */
    public void addChest(@NotNull String rarity, @NotNull Player player){
        ChestsConfig.addLocation(player.getLocation(), rarity);
        player.sendMessage(Messages.text("event.add-chest.done"));

        var message = Messages.getString("logs.new-chest")
                .replace("{count}", String.valueOf(ChestsConfig.getCount()));
        logger.info(message);
    }

    /**
     * Генерирует список сундуков, ограниченный максимальным количесвом по конфигу или
     * размеру списка доступных сундуков. Предотвращает дублирование.
     * @return список моделей для спавна.
     */
    private List<ChestInfoModel> chestsToSpawn(){
        var maxChestsCount = Config.getInt("chests-to-spawn");
        var chestsList = ChestsConfig.getAllChests();

        if (maxChestsCount <= 0 || chestsList.isEmpty()) {
            return Collections.emptyList();
        }

        //Если запрашивается больше сундуков, чем есть в наличии, ограничиваем количество доступными сундуками.
        maxChestsCount = Math.min(maxChestsCount, chestsList.size());

        Collections.shuffle(chestsList, ThreadLocalRandom.current());
        return chestsList.subList(0, maxChestsCount);

//        var list = new ArrayList<ChestInfoModel>();
//
//        var spawnIdxes = new ArrayList<Integer>();
//        for(int i = 0; i < maxChestsCount; i++){
//            spawnIdxes.add(ThreadLocalRandom.current().nextInt(0, chestsList.size()));
//        }
//
//        for(var chestIndex: spawnIdxes){
//            list.add(chestsList.get(chestIndex));
//        }
//
//        return list;
    }

    /**
     * Заполняет сундук предметами. Если предметов больше чем слотов - заполняет все слоты
     * @param chest сундук
     * @param items предметы для заполнения
     */
    private void fillChestRandom(Chest chest, Set<ItemStack> items){
        if(items.isEmpty()) return;

        // Если предметов больше или равно количеству слотов - заполняем все слоты
        if(items.size() >= MAX_SLOT_INDEX){
            var arrItems = items.toArray(new ItemStack[0]);

            for(int i = 0; i < MAX_SLOT_INDEX; i++){
                chest.getInventory().setItem(i, arrItems[0]);
            }

            return;
        }

        // Если предметов меньше слотов - размещаем их в случайных позициях
        for(var entry: generatePositions(items).entrySet()){
            chest.getInventory().setItem(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Генерирует словарь [слот ид - предмет] для рандомного заполнениня слотов предметами.
     * Можно переписать на менее читабельный, но "более быстрый" код.
     * @param items предметы
     * @return словарь
     */
    private Map<Integer, ItemStack> generatePositions(Set<ItemStack> items){
        if(items.isEmpty()) return Collections.emptyMap();

        var positions = generateRandomSlots(items.size());
        var itemsIter = items.iterator();

        var result = new HashMap<Integer, ItemStack>();
        for(int pos: positions){
            result.put(pos, itemsIter.next());
        }

        return result;
    }

    /**
     * Генерирует набор рандомных слотов в пределах константы. Дублирование исключено.
     * @param itemsCount количество предметов = количество слотов
     * @return список слотов
     */
    private ArrayList<Integer> generateRandomSlots(int itemsCount){
        if(itemsCount <= 0) return new ArrayList<>();

        var usedSlots = new HashSet<Integer>();
        var result = new ArrayList<Integer>();
        var random = ThreadLocalRandom.current();

        while (result.size() < itemsCount){
            var slot = random.nextInt(MAX_SLOT_INDEX);
            if(usedSlots.add(slot))
                result.add(slot);
        }

        return result;
    }

    private BlockFace yawToCardinal(float yaw) {
        yaw = (yaw % 360 + 360) % 360;

        if (yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            return BlockFace.WEST;
        } else if (yaw >= 135 && yaw < 225) {
            return BlockFace.NORTH;
        } else {
            return BlockFace.EAST;
        }
    }

    public void removePrevious(){
        List<Location> chests = ChestsConfig.getAllLocations();
        for(Location location: chests){
            if(location.getBlock().getType() == Material.CHEST){
                var chest = (Chest) location.getBlock().getState();
                chest.getInventory().setContents(new ItemStack[0]);

                location.getBlock().setType(Material.AIR);
            }
        }
    }
}
