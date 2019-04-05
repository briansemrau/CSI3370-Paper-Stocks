package com.csi3370.paperstocks.web.rest.errors;

import com.csi3370.paperstocks.web.rest.PortfolioResource;

public class PortfolioNotEmptyException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PortfolioNotEmptyException() {
        super("Can't delete a portfolio that still contains shares.", "portfolio", "idnull");
    }

}
