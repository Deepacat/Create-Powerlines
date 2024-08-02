package net.deepacat.createpowerlines.energy.network;

import java.util.Optional;

public interface EnergyNetworkProvider {
	Optional<EnergyNetwork> getNetwork();
}
