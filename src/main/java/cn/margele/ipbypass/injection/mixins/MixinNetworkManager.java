package cn.margele.ipbypass.injection.mixins;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet packetIn, CallbackInfo callbackInfo) {
    	try {
        	if (packetIn instanceof C00Handshake) {
        		C00Handshake handshakePacket = (C00Handshake)packetIn;
        		Class clazz = handshakePacket.getClass();
        		for (Field field : clazz.getDeclaredFields()) {
        			if (field.getType() == String.class) {
        				field.setAccessible(true);
        				String targetIP = field.get(handshakePacket).toString();
        				if (targetIP.contains("hypixel")) {
        					System.out.println("Redirect to Hypixel");
        					field.set(handshakePacket, "mc.hypixel.net");
        				}
        			}
        		}
        	}
    	} catch (Throwable e) {
    		e.printStackTrace();
    	}
    }
}