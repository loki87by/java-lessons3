package org.example.item;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ItemRepositoryCustom  {
List<ItemInfoWithUrlState> findAndCheckLinkValidityAndSaveStatus ();
}
