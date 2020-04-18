package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Veiculo;

public class VeiculoServicos {

	public List<Veiculo> findAll() {

		List<Veiculo> list = new ArrayList<>();

		list.add(new Veiculo("PSP-1717", "KA", "BRANCO", "14/03/2017"));
		list.add(new Veiculo("DSD-1456", "MAREA", "ROXO", "10/07/2000"));
		list.add(new Veiculo("POI-1714", "KWID", "BRANCO", "17/05/2018"));
		list.add(new Veiculo("DEC-1010", "POLO", "PRETO", "24/07/2020"));
		list.add(new Veiculo("SAC-1477", "HRV", "VINHO", "10/01/2016"));

		return list;
	}
}