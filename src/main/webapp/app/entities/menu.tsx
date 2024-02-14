import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/stock">
        <Translate contentKey="global.menu.entities.stock" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reservation">
        <Translate contentKey="global.menu.entities.reservation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/invoice">
        <Translate contentKey="global.menu.entities.invoice" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
