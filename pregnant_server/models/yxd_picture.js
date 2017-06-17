'use strict';
module.exports = function(sequelize, DataTypes) {
  var yxd_pictures = sequelize.define('yxd_pictures', {
    mac_id: DataTypes.STRING,
    left_dpi: DataTypes.INTEGER,//
    right_dpi: DataTypes.INTEGER,//
    left_url:DataTypes.STRING,//
    right_url:DataTypes.STRING,
    left_urla:DataTypes.STRING,//
    right_urla:DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return yxd_pictures;
};