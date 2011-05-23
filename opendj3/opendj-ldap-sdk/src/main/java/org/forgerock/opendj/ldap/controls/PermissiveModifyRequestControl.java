/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opendj3/legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opendj3/legal-notices/CDDLv1_0.txt.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2010 Sun Microsystems, Inc.
 */
package org.forgerock.opendj.ldap.controls;



import static org.forgerock.opendj.ldap.CoreMessages.ERR_PERMISSIVE_MODIFY_CONTROL_BAD_OID;
import static org.forgerock.opendj.ldap.CoreMessages.ERR_PERMISSIVE_MODIFY_INVALID_CONTROL_VALUE;

import org.forgerock.i18n.LocalizableMessage;
import org.forgerock.opendj.ldap.*;

import com.forgerock.opendj.util.Validator;



/**
 * The Microsoft defined permissive modify request control. The OID for this
 * control is 1.2.840.113556.1.4.1413, and it does not have a value.
 * <p>
 * This control can only be used with LDAP modify requests. It changes the
 * behavior of the modify operation as follows:
 * <ul>
 * <li>Attempts to add an attribute value which already exists will be ignored
 * and will not cause an {@link ResultCode#ATTRIBUTE_OR_VALUE_EXISTS
 * AttributeValueExists} error result to be returned.
 * <li>Attempts to delete an attribute value which does not exist will be
 * ignored and will not cause an {@link ResultCode#NO_SUCH_ATTRIBUTE
 * NoSuchAttribute} error result to be returned.
 * </ul>
 * In other words, a modify request {@code add} modification <i>ensures</i> that
 * the attribute contains the specified attribute value, and a {@code delete}
 * modification <i>ensures</i> that the attribute does not contain the specified
 * attribute value.
 */
public final class PermissiveModifyRequestControl implements Control
{
  /**
   * The OID for the permissive modify request control.
   */
  public static final String OID = "1.2.840.113556.1.4.1413";

  private static final PermissiveModifyRequestControl CRITICAL_INSTANCE =
    new PermissiveModifyRequestControl(true);

  private static final PermissiveModifyRequestControl NONCRITICAL_INSTANCE =
    new PermissiveModifyRequestControl(false);

  /**
   * A decoder which can be used for decoding the permissive modify request
   * control.
   */
  public static final ControlDecoder<PermissiveModifyRequestControl> DECODER =
    new ControlDecoder<PermissiveModifyRequestControl>()
  {

    public PermissiveModifyRequestControl decodeControl(final Control control,
        final DecodeOptions options) throws DecodeException
    {
      Validator.ensureNotNull(control);

      if (control instanceof PermissiveModifyRequestControl)
      {
        return (PermissiveModifyRequestControl) control;
      }

      if (!control.getOID().equals(OID))
      {
        final LocalizableMessage message = ERR_PERMISSIVE_MODIFY_CONTROL_BAD_OID
            .get(control.getOID(), OID);
        throw DecodeException.error(message);
      }

      if (control.hasValue())
      {
        final LocalizableMessage message = ERR_PERMISSIVE_MODIFY_INVALID_CONTROL_VALUE
            .get();
        throw DecodeException.error(message);
      }

      return control.isCritical() ? CRITICAL_INSTANCE : NONCRITICAL_INSTANCE;
    }



    public String getOID()
    {
      return OID;
    }
  };



  /**
   * Creates a new permissive modify request control having the provided
   * criticality.
   *
   * @param isCritical
   *          {@code true} if it is unacceptable to perform the operation
   *          without applying the semantics of this control, or {@code false}
   *          if it can be ignored.
   * @return The new control.
   */
  public static PermissiveModifyRequestControl newControl(
      final boolean isCritical)
  {
    return isCritical ? CRITICAL_INSTANCE : NONCRITICAL_INSTANCE;
  }



  private final boolean isCritical;



  private PermissiveModifyRequestControl(final boolean isCritical)
  {
    this.isCritical = isCritical;
  }



  /**
   * {@inheritDoc}
   */
  public String getOID()
  {
    return OID;
  }



  /**
   * {@inheritDoc}
   */
  public ByteString getValue()
  {
    return null;
  }



  /**
   * {@inheritDoc}
   */
  public boolean hasValue()
  {
    return false;
  }



  /**
   * {@inheritDoc}
   */
  public boolean isCritical()
  {
    return isCritical;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("PermissiveModifyRequestControl(oid=");
    builder.append(getOID());
    builder.append(", criticality=");
    builder.append(isCritical());
    builder.append(")");
    return builder.toString();
  }

}
