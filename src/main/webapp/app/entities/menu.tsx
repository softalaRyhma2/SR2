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

  const hasTransportAuthority = () => {
    return currentUserAuthorities?.some(authority => authority === 'ROLE_TRANSPORT');
  };
  const hasADMINAuthority = () => {
    return currentUserAuthorities?.some(authority => authority === 'ROLE_ADMIN');
  };
  const hasRECSERAuthority = () => {
    return currentUserAuthorities?.some(authority => authority === 'ROLE_RECSER');
  };

  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/invoice">
        <Translate contentKey="global.menu.entities.invoice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/stock">
        <Translate contentKey="global.menu.entities.stock" />
      </MenuItem>
      {hasADMINAuthority() && (
        <MenuItem icon="asterisk" to="/stock-item">
          <Translate contentKey="global.menu.entities.stockItem" />
        </MenuItem>
      )}
      {!hasTransportAuthority() && !hasPCENTERAuthority() && (
        <MenuItem icon="asterisk" to="/stock-item-type">
          <Translate contentKey="global.menu.entities.stockItemType" />
        </MenuItem>
      )}
      <MenuItem icon="asterisk" to="/stock-item-type-company">
        <Translate contentKey="global.menu.entities.stockItemTypeCompany" />
      </MenuItem>
      {!hasPCENTERAuthority() && ( // DON'T render if pcenter auth
        <MenuItem icon="asterisk" to="/reservation">
          <Translate contentKey="global.menu.entities.reservation" />
        </MenuItem>
      )}
      {!hasTransportAuthority() && !hasPCENTERAuthority() && (
        <MenuItem icon="asterisk" to="/reserved-item">
          <Translate contentKey="global.menu.entities.reservedItem" />
        </MenuItem>
      )}
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
