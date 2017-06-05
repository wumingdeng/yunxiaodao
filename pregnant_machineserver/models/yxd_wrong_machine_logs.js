'use strict';
module.exports = function(sequelize, DataTypes) {
  var yxd_wrong_machine_logs = sequelize.define('yxd_wrong_machine_logs', {
    ipaddress: DataTypes.STRING,
    wrong_mac: DataTypes.STRING,
  },{
       timestamps: true
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return yxd_wrong_machine_logs;
};