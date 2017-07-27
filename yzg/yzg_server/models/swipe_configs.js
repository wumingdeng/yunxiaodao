'use strict';
module.exports = function(sequelize, DataTypes) {
  var swipe_configs = sequelize.define('swipe_configs', {
    url: DataTypes.STRING,
    showtime: DataTypes.INTEGER,
    place:DataTypes.INTEGER
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return swipe_configs;
};