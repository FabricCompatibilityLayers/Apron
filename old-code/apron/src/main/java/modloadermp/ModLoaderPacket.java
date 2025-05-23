package modloadermp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.api.ApronApi;

@Legacy
public class ModLoaderPacket extends Packet {
	private static final ApronApi APRON = ApronApi.getInstance();
	private static final int MAX_DATA_LENGTH = 0xFFFF;

	public int modId;
	public int packetType;
	public int[] dataInt = new int[0];
	public float[] dataFloat = new float[0];
	public String[] dataString = new String[0];
	@Environment(EnvType.SERVER)
	private static Map<NetworkHandler, ServerPlayerEntity> playerMap;

	static {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			playerMap = new HashMap<>();
		}
	}

	public ModLoaderPacket() {
	}

	@Override
	public void read(DataInputStream datainputstream) {
		try {
			this.modId = datainputstream.readInt();
			this.packetType = datainputstream.readInt();
			int i = datainputstream.readInt();

			if (i > MAX_DATA_LENGTH) {
				throw new IOException(String.format("Integer data size of %d is higher than the max (%d).", i, MAX_DATA_LENGTH));
			} else {
				this.dataInt = new int[i];

				for (int j = 0; j < i; ++j) {
					this.dataInt[j] = datainputstream.readInt();
				}

				int k = datainputstream.readInt();

				if (k > MAX_DATA_LENGTH) {
					throw new IOException(String.format("Float data size of %d is higher than the max (%d).", k, MAX_DATA_LENGTH));
				} else {
					this.dataFloat = new float[k];

					for (int l = 0; l < k; ++l) {
						this.dataFloat[l] = datainputstream.readFloat();
					}

					int i1 = datainputstream.readInt();

					if (i1 > MAX_DATA_LENGTH) {
						throw new IOException(String.format("String data size of %d is higher than the max (%d).", i1, MAX_DATA_LENGTH));
					} else {
						this.dataString = new String[i1];

						for (int j1 = 0; j1 < i1; ++j1) {
							int k1 = datainputstream.readInt();

							if (k1 > MAX_DATA_LENGTH) {
								throw new IOException(String.format("String length of %d is higher than the max (%d).", k1, MAX_DATA_LENGTH));
							}

							byte[] abyte0 = new byte[k1];
							datainputstream.read(abyte0, 0, k1);
							this.dataString[j1] = new String(abyte0);
						}
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(DataOutputStream dataoutputstream) {
		try {
			if (this.dataInt != null && this.dataInt.length > MAX_DATA_LENGTH) {
				throw new IOException(String.format("Integer data size of %d is higher than the max (%d).", this.dataInt.length, MAX_DATA_LENGTH));
			} else if (this.dataFloat != null && this.dataFloat.length > MAX_DATA_LENGTH) {
				throw new IOException(String.format("Float data size of %d is higher than the max (%d).", this.dataFloat.length, MAX_DATA_LENGTH));
			} else if (this.dataString != null && this.dataString.length > MAX_DATA_LENGTH) {
				throw new IOException(String.format("String data size of %d is higher than the max (%d).", this.dataString.length, MAX_DATA_LENGTH));
			} else {
				dataoutputstream.writeInt(this.modId);
				dataoutputstream.writeInt(this.packetType);

				if (this.dataInt == null) {
					dataoutputstream.writeInt(0);
				} else {
					dataoutputstream.writeInt(this.dataInt.length);

					for (int j : this.dataInt) {
						dataoutputstream.writeInt(j);
					}
				}

				if (this.dataFloat == null) {
					dataoutputstream.writeInt(0);
				} else {
					dataoutputstream.writeInt(this.dataFloat.length);

					for (float v : this.dataFloat) {
						dataoutputstream.writeFloat(v);
					}
				}

				if (this.dataString == null) {
					dataoutputstream.writeInt(0);
				} else {
					dataoutputstream.writeInt(this.dataString.length);

					for (String s : this.dataString) {
						if (s.length() > MAX_DATA_LENGTH) {
							throw new IOException(String.format("String length of %d is higher than the max (%d).", s.length(), MAX_DATA_LENGTH));
						}

						dataoutputstream.writeInt(s.length());
						dataoutputstream.writeBytes(s);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void apply(NetworkHandler netHandler) {
		if (APRON.isClient()) {
			ModLoaderMp.HandleAllPackets(this);
		} else {
			ServerPlayerEntity player = null;

			if (playerMap.containsKey(netHandler)) {
				player = playerMap.get(netHandler);
			} else if (netHandler instanceof ServerPlayNetworkHandler) {
				player = ((ServerPlayNetworkHandler) netHandler).field_920;
			}

			ModLoaderMp.HandleAllPackets(this, player);
		}
	}

	@Override
	public int size() {
		int i = 1;
		++i;
		i = ++i + (this.dataInt != null ? this.dataInt.length * 32 : 0);
		i = ++i + (this.dataFloat != null ? this.dataFloat.length * 32 : 0);
		++i;

		if (this.dataString != null) {
			for (String s : this.dataString) {
				i = ++i + s.length();
			}
		}

		return i;
	}
}
