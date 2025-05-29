package modloadermp;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet230ModLoader extends Packet {
	private static final int MAX_DATA_LENGTH = 65535;
	public int modId;
	public int packetType;
	public int[] dataInt = new int[0];
	public float[] dataFloat = new float[0];
	public String[] dataString = new String[0];

	public void read(DataInputStream datainputstream) {
		try {
			this.modId = datainputstream.readInt();
			this.packetType = datainputstream.readInt();
			int i = datainputstream.readInt();
			if (i > 65535) {
				throw new IOException(String.format("Integer data size of %d is higher than the max (%d).", i, 65535));
			} else {
				this.dataInt = new int[i];

				for (int j = 0; j < i; ++j) {
					this.dataInt[j] = datainputstream.readInt();
				}

				int k = datainputstream.readInt();
				if (k > 65535) {
					throw new IOException(String.format("Float data size of %d is higher than the max (%d).", k, 65535));
				} else {
					this.dataFloat = new float[k];

					for (int l = 0; l < k; ++l) {
						this.dataFloat[l] = datainputstream.readFloat();
					}

					int i1 = datainputstream.readInt();
					if (i1 > 65535) {
						throw new IOException(String.format("String data size of %d is higher than the max (%d).", i1, 65535));
					} else {
						this.dataString = new String[i1];

						for (int j1 = 0; j1 < i1; ++j1) {
							int k1 = datainputstream.readInt();
							if (k1 > 65535) {
								throw new IOException(String.format("String length of %d is higher than the max (%d).", k1, 65535));
							}

							byte[] abyte0 = new byte[k1];
							datainputstream.read(abyte0, 0, k1);
							this.dataString[j1] = new String(abyte0);
						}

					}
				}
			}
		} catch (IOException ioexception) {
			throw new RuntimeException(ioexception);
		}
	}

	public void write(DataOutputStream dataoutputstream) {
		try {
			if (this.dataInt != null && this.dataInt.length > 65535) {
				throw new IOException(String.format("Integer data size of %d is higher than the max (%d).", this.dataInt.length, 65535));
			} else if (this.dataFloat != null && this.dataFloat.length > 65535) {
				throw new IOException(String.format("Float data size of %d is higher than the max (%d).", this.dataFloat.length, 65535));
			} else if (this.dataString != null && this.dataString.length > 65535) {
				throw new IOException(String.format("String data size of %d is higher than the max (%d).", this.dataString.length, 65535));
			} else {
				dataoutputstream.writeInt(this.modId);
				dataoutputstream.writeInt(this.packetType);
				if (this.dataInt == null) {
					dataoutputstream.writeInt(0);
				} else {
					dataoutputstream.writeInt(this.dataInt.length);

					for (int i = 0; i < this.dataInt.length; ++i) {
						dataoutputstream.writeInt(this.dataInt[i]);
					}
				}

				if (this.dataFloat == null) {
					dataoutputstream.writeInt(0);
				} else {
					dataoutputstream.writeInt(this.dataFloat.length);

					for (int j = 0; j < this.dataFloat.length; ++j) {
						dataoutputstream.writeFloat(this.dataFloat[j]);
					}
				}

				if (this.dataString == null) {
					dataoutputstream.writeInt(0);
				} else {
					dataoutputstream.writeInt(this.dataString.length);

					for (int k = 0; k < this.dataString.length; ++k) {
						if (this.dataString[k].length() > 65535) {
							throw new IOException(String.format("String length of %d is higher than the max (%d).", this.dataString[k].length(), 65535));
						}

						dataoutputstream.writeInt(this.dataString[k].length());
						dataoutputstream.writeBytes(this.dataString[k]);
					}
				}

			}
		} catch (IOException ioexception) {
			throw new RuntimeException(ioexception);
		}
	}

	public void apply(NetworkHandler nethandler) {
		ModLoaderMp.HandleAllPackets(this);
	}

	public int size() {
		int i = 1;
		++i;
		++i;
		i += this.dataInt != null ? this.dataInt.length * 32 : 0;
		++i;
		i += this.dataFloat != null ? this.dataFloat.length * 32 : 0;
		++i;
		if (this.dataString != null) {
			for(int j = 0; j < this.dataString.length; ++j) {
				++i;
				i += this.dataString[j].length();
			}
		}

		return i;
	}
}
