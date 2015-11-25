package com.sofactory.capitalcycles.cycletrip.DTOs;

import java.util.List;

/**
 * Created by LuisSebastian on 10/18/15.
 */
public class CatalogoDTO extends RespuestaDTO{

    private List<ProductoDTO> productos;

    public List<ProductoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoDTO> productos) {
        this.productos = productos;
    }
}
