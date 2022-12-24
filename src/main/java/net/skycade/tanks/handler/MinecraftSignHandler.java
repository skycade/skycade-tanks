package net.skycade.tanks.handler;

import java.util.Arrays;
import java.util.Collection;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public final class MinecraftSignHandler implements BlockHandler {

  @NotNull
  @Override
  public NamespaceID getNamespaceId() {
    return NamespaceID.from("minecraft", "sign");
  }

  @NotNull
  public Collection<Tag<?>> getBlockEntityTags() {
    return Arrays.asList(Tag.Byte("GlowingText"), Tag.String("Color"), Tag.String("Text1"),
        Tag.String("Text2"), Tag.String("Text3"), Tag.String("Text4"));
  }
}
