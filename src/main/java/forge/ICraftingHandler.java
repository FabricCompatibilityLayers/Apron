/**
 * Minecraft Forge Public Licence
 * <p>
 * ==============================
 * <p>
 * <p>
 * Version 1.1
 * <p>
 * <p>
 * 0. Definitions
 * <p>
 * --------------
 * <p>
 * <p>
 * Minecraft: Denotes a copy of the Minecraft game licensed by Mojang AB
 * <p>
 * <p>
 * User: Anybody that interact with the software in one of the following ways:
 * <p>
 * - play
 * <p>
 * - decompile
 * <p>
 * - recompile or compile
 * <p>
 * - modify
 * <p>
 * <p>
 * Minecraft Forge: The Minecraft Forge code, in source form, class file form, as
 * <p>
 * obtained in a standalone fashion or as part of a wider distribution.
 * <p>
 * <p>
 * Dependency: Code required to have Minecraft Forge working properly. That can
 * <p>
 * include dependencies required to compile the code as well as modifications in
 * <p>
 * the Minecraft sources that are required to have Minecraft Forge working.
 * <p>
 * <p>
 * 1. Scope
 * <p>
 * --------
 * <p>
 * <p>
 * The present license is granted to any user of Minecraft Forge. As a
 * <p>
 * prerequisite, a user of Minecraft Forge must own a legally aquired copy of
 * <p>
 * Minecraft
 * <p>
 * <p>
 * 2. Play rights
 * <p>
 * --------------
 * <p>
 * <p>
 * The user of Minecraft Forge is allowed to install the software on a client or
 * <p>
 * a server and to play it without restriction.
 * <p>
 * <p>
 * 3. Modification rights
 * <p>
 * ----------------------
 * <p>
 * <p>
 * The user has the right to decompile the source code, look at either the
 * <p>
 * decompiled version or the original source code, and to modify it.
 * <p>
 * <p>
 * 4. Derivation rights
 * <p>
 * --------------------
 * <p>
 * <p>
 * The user has the rights to derive code from Minecraft Forge, that is to say to
 * <p>
 * write code that either extends Minecraft Forge class and interfaces,
 * <p>
 * instantiate the objects declared or calls the functions. This code is known as
 * <p>
 * "derived" code, and can be licensed with conditions different from Minecraft
 * <p>
 * Forge.
 * <p>
 * <p>
 * <p>
 * 5. Distribution rights
 * <p>
 * ----------------------
 * <p>
 * <p>
 * The user of Minecraft Forge is allowed to redistribute Minecraft Forge in
 * <p>
 * partially, in totality, or included in a distribution. When distributing
 * <p>
 * binaries or class files, the user must provide means to obtain the sources of
 * <p>
 * the distributed version of Minecraft Forge at no costs. This includes the
 * <p>
 * files as well as any dependency that the code may rely on, including patches to
 * <p>
 * minecraft original sources.
 * <p>
 * <p>
 * Modification of Minecraft Forge as well as dependencies, including patches to
 * <p>
 * minecraft original sources, has to remain under the terms of the present
 * <p>
 * license.
 */
package forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface ICraftingHandler {
	void onTakenFromCrafting(PlayerEntity arg, ItemStack arg2, Inventory arg3);
}
