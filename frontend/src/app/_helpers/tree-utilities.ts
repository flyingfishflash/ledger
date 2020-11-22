export function list_to_tree_sorted(list) {
  const map = {};
  let node;
  const roots = [];
  let i;
  let minId = 0;
  let maxId = 0;
  let minParentId = 0;

  /*
        Get min id, max id, min parent id
        The minumum parent id will be considered the root account
    */

  minId = list[0].id;
  maxId = list[0].id;
  minParentId = list[0].parentId;

  for (i = 0; i < list.length; i += 1) {
    if (list[i].id < minId) {
      minId = list[i].id;
    }
    if (list[i].id > maxId) {
      maxId = list[i].id;
    }
    if (list[i].parentId < minParentId) {
      minParentId = list[i].parentId;
    }
  }
  console.log("minId: " + minId);
  console.log("maxId: " + maxId);
  console.log("minParentId: " + minParentId);

  for (i = 0; i <= maxId - minId; i += 1) {
    map[list[i].id] = i; // initialize the map
    list[i].children = []; // initialize the children
  }

  for (i = 0; i <= maxId - minId; i += 1) {
    node = list[i];

    if (node.parentId !== minParentId) {
      // if you have dangling branches check that map[node.parentId] exists
      list[map[node.parentId]].children.push(node);
    } else {
      roots.push(node);
    }
  }
  // console.log(roots);
  return roots;
}

export function list_to_tree_unsorted(list) {
  /**
   * Slower.
   * For use when tree structure is potentially unsorted.
   */

  let i = 0;
  const tree = [];
  const mappedArr = {};
  let arrElem;
  let mappedElem;
  let minId = 0;
  let maxId = 0;
  let minParentId = 0;

  /*
            Get min id, max id, min parent id
            The minumum parent id will be considered the root account
        */

  minId = list[0].id;
  maxId = list[0].id;
  minParentId = list[0].parentId;

  for (i = 0; i < list.length; i += 1) {
    if (list[i].id < minId) {
      minId = list[i].id;
    }
    if (list[i].id > maxId) {
      maxId = list[i].id;
    }
    if (list[i].parentId < minParentId) {
      minParentId = list[i].parentId;
    }
  }

  console.log("minId: " + minId);
  console.log("maxId: " + maxId);
  console.log("minParentId: " + minParentId);

  // First map the nodes of the array to an object -> create a hash table.
  for (i = 0; i <= maxId - minId; i++) {
    arrElem = list[i];
    mappedArr[arrElem.id] = arrElem;
    mappedArr[arrElem.id]["children"] = [];
  }

  // console.log(mappedArr);

  for (const id in mappedArr) {
    if (mappedArr.hasOwnProperty(id)) {
      mappedElem = mappedArr[id];
      // If the element is not at the root level, add it to its parent array of children.
      if (mappedElem.parentId !== 27) {
        mappedArr[mappedElem["parentId"]]["children"].push(mappedElem);
      } else {
        // If the element is at the root level, add it to first level elements array.
        tree.push(mappedElem);
      }
    }
  }
  // console.log(tree);
  return tree;
}
