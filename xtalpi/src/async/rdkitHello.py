# -*- encoding: utf-8 -*-

from rdkit import Chem
m = Chem.MolFromSmiles('Cc1ccccc1')

# Smiles:
# Mol：摩尔 （物质的量）
help(m)
print()