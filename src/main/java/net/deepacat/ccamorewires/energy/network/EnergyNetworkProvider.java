package net.deepacat.ccamorewires.energy.network;

import java.util.Optional;

public interface EnergyNetworkProvider {
	Optional<EnergyNetwork> getNetwork();
}
