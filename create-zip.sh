
set -x

REPO_NAME=$(basename "$(pwd)")
ZIP_NAME=$REPO_NAME-$(date +%Y%m%d).zip
cd ..

zip -r $ZIP_NAME $REPO_NAME \
 -x "./$REPO_NAME/.gradle/*" \
 -x "./$REPO_NAME/.idea/*" \
 -x "./$REPO_NAME/build/*" \
 -x "./$REPO_NAME/target/*" \
 -x "./$REPO_NAME/.git/*" \
 -x "./$REPO_NAME/compose/data/*"

echo "Created \"$ZIP_NAME\" in parent folder"
ls -lh $ZIP_NAME

cd -