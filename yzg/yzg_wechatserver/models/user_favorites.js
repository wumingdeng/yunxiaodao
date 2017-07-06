'use strict';
module.exports = function(sequelize, DataTypes) {
  var user_favorites = sequelize.define('user_favorites', {
    wid: DataTypes.INTEGER,
    uid: DataTypes.INTEGER
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return user_favorites;
};