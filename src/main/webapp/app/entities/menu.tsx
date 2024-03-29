import React from 'react';
import { Translate } from 'react-jhipster';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { useAppSelector } from 'app/config/store';
import store from 'app/config/store'; // redux import

const EntitiesMenu = () => {
  const currentUserAuthorities = useAppSelector(state => state.authentication.account?.authorities);

  const hasPCENTERAuthority = () => {
    return currentUserAuthorities?.some(authority => authority === 'ROLE_PCENTER');
  };

  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/stock">
        <Translate contentKey="global.menu.entities.stock" />
      </MenuItem>
      {!hasPCENTERAuthority() && ( // render if pcenter auth
        <MenuItem icon="asterisk" to="/reservation">
          <Translate contentKey="global.menu.entities.reservation" />
        </MenuItem>
      )}
      <MenuItem icon="asterisk" to="/invoice">
        <Translate contentKey="global.menu.entities.invoice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/stock-item">
        <Translate contentKey="global.menu.entities.stockItem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/stock-item-type">
        <Translate contentKey="global.menu.entities.stockItemType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reserved-item">
        <Translate contentKey="global.menu.entities.reservedItem" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
