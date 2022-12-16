package net.skycade.tanks.space;

import java.time.Duration;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.block.Block;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.serverruntime.space.dimension.FullbrightDimension;

public class TestGameSpace extends GameSpace {

  public static TestGameSpace INSTANCE = new TestGameSpace();

  /**
   * Constructor for the game space.
   */
  public TestGameSpace() {
    super(UUID.randomUUID(), FullbrightDimension.INSTANCE);
  }

  /**
   * Initialize the game space.
   */
  @Override
  public void init() {
    setGenerator(unit -> unit.modifier().fillHeight(0, 20, Block.STONE));
    setTimeRate(0);

    EventNode<InstanceEvent> eventNode = eventNode();
    eventNode.addListener(AddEntityToInstanceEvent.class, event -> {
      final Entity entity = event.getEntity();
      if (entity instanceof Player player) {
        if (player.getInstance() == null) {
          MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            // todo: implement onFirstSpawn
            player.showTitle(Title.title(Component.text("You joined tanks", NamedTextColor.GREEN),
                Component.text(""), Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3),
                    Duration.ofSeconds(1))));
          });
        }
      }
    }).addListener(ItemDropEvent.class, event -> event.setCancelled(true));
  }
}
